package com.automa.ui.common

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.automa.ui.databinding.ActivityCameraXBinding
import com.automa.ui.utils.toast
import com.bumptech.glide.load.ImageHeaderParser.UNKNOWN_ORIENTATION
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraXActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraXBinding

    private var imageCapture: ImageCapture ?= null
    private var imageAnalysis: ImageAnalysis ?= null
    private lateinit var outDir: File
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val OUTPUT_FILE_PATH = "OUTPUT_PATH"
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == UNKNOWN_ORIENTATION) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageAnalysis?.targetRotation = rotation
                imageCapture?.targetRotation = rotation
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXBinding.inflate(layoutInflater)
        setContentView(binding.root)
        outDir = getOutputDirectory()

        startCamera()

        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val file = File(outDir, System.currentTimeMillis().toString()+".jpg")
        val outputOption = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(outputOption, ContextCompat.getMainExecutor(this@CameraXActivity),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(file)
                getLocation { location ->
                    addMetadataToImage(file, location)
                    setResult(Activity.RESULT_OK, intent.putExtra(OUTPUT_FILE_PATH, savedUri.toString()))
                    finish()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                toast(this@CameraXActivity, "Error Occurred while saving photo")
            }
        })
    }

    private fun startCamera() {
        val cameraProvider = ProcessCameraProvider.getInstance(this)
        val controller: CameraController = LifecycleCameraController(this).apply { bindToLifecycle(this@CameraXActivity) }
        binding.viewFinderCamera.controller = controller

        cameraProvider.addListener({
            val camProvider: ProcessCameraProvider = cameraProvider.get()
            val preview = Preview.Builder().build().also { mPreview ->
                mPreview.setSurfaceProvider(
                    binding.viewFinderCamera.surfaceProvider
                )
            }

            imageAnalysis = ImageAnalysis.Builder().build()
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                camProvider.unbindAll()
                camProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, "com.automa.app").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun addMetadataToImage(file: File, location: Location?) {
        try {
            val exif = ExifInterface(file.absolutePath)

            // Add timestamp
            val timestamp = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
            val formattedTimestamp = sdf.format(Date(timestamp))
            exif.setAttribute(ExifInterface.TAG_DATETIME, formattedTimestamp)

            // Add location
            location?.let { coordinate ->
                exif.setLatLong(coordinate.latitude, coordinate.longitude)
            }
            // Save the attributes
            exif.saveAttributes()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(onLocationRetrieved: (Location?) -> Unit) {
        // Get the last known location, if available
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                // Location retrieved successfully
                onLocationRetrieved(location)
            } else {
                // If the last known location is not available, request an update
                val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).apply {
                    setMinUpdateIntervalMillis(0)
                    setMaxUpdates(1)
                }.build()

                fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        fusedLocationClient.removeLocationUpdates(this) // Stop updates
                        onLocationRetrieved(locationResult.lastLocation)
                    }
                }, Looper.getMainLooper())
            }
        }.addOnFailureListener {
            // Handle failure in retrieving the location
            onLocationRetrieved(null)
        }
    }

    private fun verifyExifData(file: File) {
        try {
            val exif = ExifInterface(file.absolutePath)

            // Verify timestamp
            val savedTimestamp = exif.getAttribute(ExifInterface.TAG_DATETIME)
            Log.d("TAG-CAMERA", "verifyExif: Saved Timestamp: $savedTimestamp")

            // Verify location (latitude and longitude)
            val latLong = exif.latLong
            if (latLong != null) {
                val latitude = latLong[0]
                val longitude = latLong[1]
                Log.d("TAG-CAMERA", "verifyExif Saved Latitude: $latitude, Longitude: $longitude")
            } else {
                Log.d("TAG-CAMERA", "verifyExif No location data saved")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}