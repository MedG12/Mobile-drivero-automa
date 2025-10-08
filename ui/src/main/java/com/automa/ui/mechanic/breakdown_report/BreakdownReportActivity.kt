package com.automa.ui.mechanic.breakdown_report

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel
import com.automa.domain.breakdown_report.model.FleetModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.custom_component.CustomIncidentImageItem
import com.automa.ui.custom_component.check_box.CheckBoxDataModel
import com.automa.ui.databinding.ActivityBreakdownReportBinding
import com.automa.ui.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class BreakdownReportActivity : BaseActivity() {
    private lateinit var binding: ActivityBreakdownReportBinding

    private val listUrgency = listOf("Low", "Medium", "High")
    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    private val imgPath = mutableListOf<String>()
    private val listImage = mutableListOf<CustomIncidentImageItem>()
//    private var selectedSubCategory: BreakdownSubCategoryModel ?= null
    private val viewModel: BreakdownReportViewModel by viewModels()
    private var selectedFleetId = -1
    private var selectedSubCategory = -1
    private var selectedUrgency = -1

    @Inject lateinit var userData: DataStore<UserDataModel>

//    private val categoryBottomSheetListener: (selectedSubCategory: BreakdownSubCategoryModel)->Unit = { selectedSubCategory ->
//        binding.customTagContainer.setTags(listOf(selectedSubCategory.name))
//        this.selectedSubCategory = selectedSubCategory
//    }

    private val pickFromCamera = registerForActivityResult(CameraActivityContract()) { result ->
        if (result != null) {
            val imageUri = Uri.parse(result)
            addImageView(ImageUtils.getPath(this, imageUri))
        }
    }

    private val pickFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        if (result != null) {
            addImageView(ImageUtils.getPath(this, result))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            pickFromCamera.launch(0)
//            DialogUtils.showImagePickerDialog(this@BreakdownReportActivity, cameraAction = {
//                pickFromCamera.launch(0)
//            }, galleryAction = {
//                pickFromGallery.launch("image/*")
//            })
        } else {
            DialogUtils.showWhiteAlertDialog(
                this,
                getString(R.string.message_permission_needed),
                getString(R.string.message_request_permission),
                positiveButton = Pair(getString(R.string.ok)) {

                }
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreakdownReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        initValidation()
        initAction()
        setUrgencyData()
        runBlocking {
            val pairedFleet = userData.data.first().driverProfile?.pairedFleet
            if (pairedFleet != null) {
                binding.atvFleet.isEnabled = false
                selectedFleetId = pairedFleet.id
                binding.atvFleet.setText(String.format("${pairedFleet.carBrands} ${pairedFleet.carType} ${pairedFleet.regNumber}"))
            } else {
                viewModel.getFleetList()
            }
        }
        viewModel.getSubCategory(0)
    }

    private fun initObserver() {
        viewModel.submitReport.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    imgPath.forEach { path ->
                        viewModel.uploadImage(success.data, binding.edtCaption.text.toString(), path)
                    }
                    DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_submit_report_success,
                        "Berhasil Membuat Laporan",
                        "Laporan Anda sedang diproses untuk tindak lanjut, silakan tunggu.",
                        positiveButton = Pair("SELESAI") {
                            startActivity(Intent(this@BreakdownReportActivity, HistoryBreakdownReportActivity::class.java))
                            finish()
                    })
                }, onFailure = { error ->
                    DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_submit_report_failed,
                        "Gagal Membuat Laporan", "Silakan ulangi dan cek laporan sebelum mengupload",
                        positiveButton = Pair("ULANGI") {

                        }, negativeButton = Pair("BATALKAN") {

                        })
                }
            )
        }

        viewModel.fleetList.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    val data = success.data.map { item -> String.format("${item.carBrands} ${item.carType} ${item.regNumber}") }.toMutableList()
                    binding.atvFleet.setOnClickListener {
                        binding.atvFleet.showDropDown()
                    }
                    binding.atvFleet.setOnItemClickListener { _, _, i, _ ->
                        parseSelectedFleet(i, success.data)
                    }
                    binding.atvFleet.setItems(data)
                }, onFailure = { error ->

                }
            )
        }

        viewModel.subCategory.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    binding.atvSubCategory.setItems(success.data.map { item -> item.name }.toMutableList())
                    binding.atvSubCategory.setOnItemClickListener { _, _, i, _ ->
                        parseSelectedSubCategory(i, success.data)
                    }
                    binding.atvSubCategory.setOnClickListener {
                        binding.atvSubCategory.showDropDown()
                    }
                }, onFailure = { error ->
                    toast(this@BreakdownReportActivity, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }
    }

    private fun initValidation() {
        binding.tilCondition.onTextValidation(binding.edtCondition)
        binding.tilDescription.onTextValidation(binding.edtDescription)
        binding.tilFleet.onTextValidation(binding.atvFleet)
        binding.tilSubCategory.onTextValidation(binding.atvSubCategory)
        binding.tilUrgency.onTextValidation(binding.edtUrgency)
    }

    private fun initAction() {
        binding.cbStoring.apply {
            initView(CheckBoxDataModel(getString(R.string.label_storing), checked = false))
            setOnCheckedChangeListener { b ->
                binding.llStoringReason.isVisible = b
            }
        }

        binding.edtUrgency.setOnClickListener {
            binding.edtUrgency.showDropDown()
        }

        binding.edtUrgency.setOnItemClickListener { _, _, i, _ ->
            selectedUrgency = parseSelectedUrgency(i)
        }

        binding.btnAddSubCategory.setOnClickListener {
//            BreakdownCategoryBottomSheetFragment(categoryBottomSheetListener).show(supportFragmentManager, "")
        }

        binding.layoutImage.llUploadPhoto.setOnClickListener {
            checkPermissions()
        }

        binding.toolbarBreakdownReport.setOnNavigationClickListener { finish() }
        binding.btnBack.setOnClickListener { finish() }

        binding.btnSubmit.setOnClickListener {
            binding.tilCondition.checkIsNotEmpty(binding.edtCondition)
            binding.tilDescription.checkIsNotEmpty(binding.edtDescription)
            binding.tilFleet.checkIsNotEmpty(binding.atvFleet)
            binding.tilSubCategory.checkIsNotEmpty(binding.atvSubCategory)
            binding.tilUrgency.checkIsNotEmpty(binding.edtUrgency)
            when {
                binding.tilCondition.error != null -> binding.edtCondition.requestFocus()
                binding.tilDescription.error != null -> binding.edtDescription.requestFocus()
                binding.tilFleet.error != null-> binding.atvFleet.requestFocus()
//                selectedSubCategory == null && binding.customTagContainer.getTags().isEmpty() -> {
                binding.tilSubCategory.error != null -> binding.atvSubCategory.requestFocus()
                binding.tilUrgency.error != null -> binding.edtUrgency.requestFocus()
                else -> {
                    viewModel.submitReport(
                        selectedSubCategory,
                        binding.edtCondition.text.toString(),
                        binding.edtDescription.text.toString(),
                        selectedFleetId,
                        selectedUrgency,
                        binding.cbStoring.getValue().checked,
                        binding.edtStoringDescription.text.toString()
                    )
                }
            }
        }
    }

    private fun setUrgencyData() {
        binding.edtUrgency.setItems(listUrgency.toMutableList())
    }

    private fun parseSelectedUrgency(selected: Int): Int {
        val urgency = listUrgency[selected]
        return when (urgency) {
            "Low" -> 3
            "Medium" -> 2
            "High" -> 1
            else -> 0
        }
    }

    private fun parseSelectedFleet(position: Int, list: List<FleetModel>) {
        return try {
            selectedFleetId = list[position].id
        } catch (e: Exception) {
            selectedFleetId = -1
        }
    }

    private fun parseSelectedSubCategory(position: Int, list: List<BreakdownSubCategoryModel>) {
        return try {
            selectedSubCategory = list[position].id
        } catch (e: Exception) {
            selectedSubCategory = -1
        }
    }

    private fun addImageView(path: String) {
        imgPath.add(path)
        val view = CustomIncidentImageItem(this)
        val onEdit = {
            toast(this, "Edit")
        }
        val onDelete = {
            imgPath.remove(path)
            listImage.remove(view)
            binding.layoutImage.llPhotoContent.removeView(view)
            binding.layoutImage.llUploadPhoto.isGone = false
        }
        view.initView(path, onEdit, onDelete, binding.clBreakdownReport)
        listImage.add(view)
        binding.layoutImage.llPhotoContent.addView(view)
        if (imgPath.size==2) binding.layoutImage.llUploadPhoto.isGone = true
    }

    private fun checkPermissions() {
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
        val locationPermissionGranted = ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED
        if (cameraPermissionGranted && locationPermissionGranted) {
            pickFromCamera.launch(0)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(CAMERA_PERMISSION) || shouldShowRequestPermissionRationale(LOCATION_PERMISSION)) {
                DialogUtils.showWhiteAlertDialog(
                    this,
                    getString(R.string.message_permission_needed),
                    getString(R.string.message_request_permission),
                    positiveButton = Pair(getString(R.string.ok)) {
                        requestMultiplePermissionsLauncher.launch(
                            arrayOf(CAMERA_PERMISSION, LOCATION_PERMISSION)
                        )
                    },
                    negativeButton = Pair(getString(R.string.cancel)) {

                    }
                )
            } else {
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(CAMERA_PERMISSION, LOCATION_PERMISSION)
                )
            }
        } else {
            requestMultiplePermissionsLauncher.launch(
                arrayOf(CAMERA_PERMISSION, LOCATION_PERMISSION)
            )
        }
    }

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraPermissionGranted = permissions[CAMERA_PERMISSION] ?: false
            val locationPermissionGranted = permissions[LOCATION_PERMISSION] ?: false

            if (cameraPermissionGranted && locationPermissionGranted) {
                pickFromCamera.launch(0)
            } else {
                DialogUtils.showWhiteAlertDialog(
                    this,
                    getString(R.string.message_permission_needed),
                    getString(R.string.message_request_permission),
                    positiveButton = Pair(getString(R.string.ok)) {

                    }
                )
            }
        }
}