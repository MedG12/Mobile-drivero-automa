package com.automa.ui.driver.task.pod

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.R
import com.automa.ui.databinding.BottomsheetUploadImageBinding
import com.automa.ui.driver.task.TaskDetailViewModel
import com.automa.ui.mechanic.task.MechanicTaskViewModel
import com.automa.ui.utils.CameraActivityContract
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.ImageUtils
import com.automa.ui.utils.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UploadPhotoBottomSheetFragment: BottomSheetDialogFragment() {
    private var _binding: BottomsheetUploadImageBinding ?= null
    private val binding get() = _binding!!

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

    private var imgPath = ""
    private val viewModel: TaskDetailViewModel by viewModels()
    private val uploadProofViewModel: MechanicTaskViewModel by viewModels()
    private var idWorkOrder = -1
    private var uploadType = ""
    private var proofType = ""

    private var idMechanicTask = -1
    private var mechanicTaskName = ""

    private var actionOnDismiss: (()->Unit) ?= null

    @Inject lateinit var userData: DataStore<UserDataModel>

    companion object {
        private const val ID_WORK_ORDER = "ID_WO"
        private const val ID_MECHANIC_TASK = "ID_MECH_TASK"
        private const val NAME_MECHANIC_TASK = "NAME_MECH_TASK"
        private const val LAYOUT_TYPE = "TYPE"
        private const val TYPE_POD = "POD"
        private const val TYPE_PROOF = "PROOF"

        private const val PROOF_TYPE = "PROOF_TYPE"
        const val PROOF_AFTER = "AFTER"
        const val PROOF_BEFORE = "BEFORE"
        fun newInstanceUploadPod(idWorkOrder: Int, taskName: String): BottomSheetDialogFragment {
            return UploadPhotoBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID_WORK_ORDER, idWorkOrder)
                    putString(NAME_MECHANIC_TASK, taskName)
                    putString(LAYOUT_TYPE, TYPE_POD)
                }
            }
        }

        fun newInstanceUploadProof(idTaskMechanic: Int, taskName: String, proofType: String, onFinish: (()-> Unit) ?= null): BottomSheetDialogFragment {
            return UploadPhotoBottomSheetFragment().apply {
                this.actionOnDismiss = onFinish
                arguments = Bundle().apply {
                    putInt(ID_MECHANIC_TASK, idTaskMechanic)
                    putString(NAME_MECHANIC_TASK, taskName)
                    putString(LAYOUT_TYPE, TYPE_PROOF)
                    putString(PROOF_TYPE, proofType)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            DialogUtils.showImagePickerDialog(requireContext(), cameraAction = {
                pickFromCamera.launch(0)
            }, galleryAction = {
                pickFromGallery.launch("image/*")
            })
        } else {
            DialogUtils.showWhiteAlertDialog(
                requireContext(),
                getString(R.string.message_permission_needed),
                getString(R.string.message_request_permission),
                positiveButton = Pair(getString(R.string.ok)) {

                }
            )
        }
    }

    private val pickFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        if (result != null) {
            imgPath = ImageUtils.getPath(requireContext(), result)
            Glide.with(requireContext())
                .applyDefaultRequestOptions(RequestOptions().override(800, 400))
                .load(imgPath).into(binding.ivImageResult)
            binding.btnUpload.isEnabled = true
        }
    }

    private val pickFromCamera = registerForActivityResult(CameraActivityContract()) { result ->
        if (result != null) {
            val imageUri = Uri.parse(result)
            imgPath = ImageUtils.getPath(requireContext(), imageUri)
            Glide.with(requireContext())
                .applyDefaultRequestOptions(RequestOptions().override(800, 400))
                .load(imgPath).into(binding.ivImageResult)
            binding.btnUpload.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetUploadImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        uploadType = args?.getString(LAYOUT_TYPE) ?: ""
        idWorkOrder = args?.getInt(ID_WORK_ORDER) ?: -1

        idMechanicTask = args?.getInt(ID_MECHANIC_TASK) ?: -1
        mechanicTaskName = args?.getString(NAME_MECHANIC_TASK) ?: ""
        proofType = args?.getString(PROOF_TYPE) ?: ""


        binding.btnBack.setOnClickListener { dismiss() }
        when (uploadType) {
            TYPE_POD -> {
                binding.tvUploadTitle.text = mechanicTaskName
                binding.btnUpload.setOnClickListener {
                    lifecycleScope.launch {
                        userData.data.first().run {
                            viewModel.uploadPod(
                                this.currentDeliveryOrder?.id ?: -1,
                                idWorkOrder,
                                binding.edtCaption.text.toString(),
                                imgPath)
                        }
                    }
                }
            }
            TYPE_PROOF -> {
                binding.tvUploadTitle.text = mechanicTaskName
                binding.btnUpload.setOnClickListener {
                    when {
                        proofType == PROOF_BEFORE -> {
                            uploadProofViewModel.uploadTaskProofBefore(
                                imgPath,
                                idMechanicTask,
                                mechanicTaskName,
                                binding.edtCaption.text.toString())
                        }
                        proofType == PROOF_AFTER -> {
                            uploadProofViewModel.uploadTaskProof(
                                imgPath,
                                idMechanicTask,
                                mechanicTaskName,
                                binding.edtCaption.text.toString())
                        }
                    }
                }
            }
        }
//        binding.llUploadPhoto.isActivated = true

        initObservable()
        binding.ivClose.setOnClickListener {
            dialog?.dismiss()
        }

        binding.llUploadPhoto.setOnClickListener {
            checkPermissions()
        }
    }

    private fun initObservable() {
        viewModel.uploadPodResult.observe(viewLifecycleOwner) {
            it.handleResult(onSuccess = { success ->
                toast(requireContext(), "Upload Berhasil")
                dismiss()
            }, onFailure = { error ->
                toast(requireContext(), "Upload Gagal ${error.errorData}")
            })
        }

        uploadProofViewModel.uploadTaskProof.observe(viewLifecycleOwner) {
            it.handleResult(onSuccess = { success ->
                toast(requireContext(), "Upload Berhasil")
                dismiss()
            }, onFailure = { error ->
                toast(requireContext(), "Upload Gagal ${error.errorData}")
            })
        }

        uploadProofViewModel.uploadTaskProofBefore.observe(viewLifecycleOwner) {
            it.handleResult(onSuccess = { success ->
                toast(requireContext(), "Upload Berhasil")
            }, onFailure = { error ->
                toast(requireContext(), "Upload Gagal ${error.errorData}")
            })
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        actionOnDismiss?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermissions() {
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
        val locationPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED
        if (cameraPermissionGranted && locationPermissionGranted) {
            pickFromCamera.launch(0)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(CAMERA_PERMISSION) || shouldShowRequestPermissionRationale(LOCATION_PERMISSION)) {
                DialogUtils.showWhiteAlertDialog(
                    requireContext(),
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
                    requireContext(),
                    getString(R.string.message_permission_needed),
                    getString(R.string.message_request_permission),
                    positiveButton = Pair(getString(R.string.ok)) {

                    }
                )
            }
        }
}
