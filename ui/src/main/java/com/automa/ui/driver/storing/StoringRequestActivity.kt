package com.automa.ui.driver.storing

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.custom_component.CustomIncidentImageItem
import com.automa.ui.databinding.ActivityStoringRequestBinding
import com.automa.ui.utils.CameraActivityContract
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.ImageUtils
import com.automa.ui.utils.toast

class StoringRequestActivity : BaseActivity() {
    private lateinit var binding: ActivityStoringRequestBinding
    private val imgPath = mutableListOf<String>()
    private val listImage = mutableListOf<CustomIncidentImageItem>()

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoringRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActions()
    }

    private val pickFromCamera = registerForActivityResult(CameraActivityContract()) { result ->
        if (result!=null) {
            val imageUri = Uri.parse(result)
            addImageView(ImageUtils.getPath(this, imageUri))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
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
    private fun initActions() {
        binding.layoutImage.llUploadPhoto.setOnClickListener {
            checkPermissions()
        }

        binding.btnBack.setOnClickListener { finish() }
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
        view.initView(path, onEdit, onDelete, binding.clStoringRequest)
        listImage.add(view)
        binding.layoutImage.llPhotoContent.addView(view)
        if (imgPath.size==3) binding.layoutImage.llUploadPhoto.isGone = true
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