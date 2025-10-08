package com.automa.ui.shared.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import com.automa.datastore.user_data.UserDataModel
import com.automa.datastore.user_data.UserRoleType
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityScannerBinding
import com.automa.ui.driver.account.PairingConfirmationActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.toast
import com.budiyev.android.codescanner.*
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ScannerActivity : BaseActivity() {
    companion object {
        private const val SCAN_TYPE = "TYPE"
        private const val SCAN_FOR_PAIRING = "PAIRING"
        private const val SCAN_FOR_CHECKING = "CHECKING"
        private const val SCAN_FOR_REFILL = "REFILL"
        private const val DATA_DELIVERY_ORDER = "DATA_DO"
        private const val DATA_MECHANIC_TASK = "DATA_MECHANIC_TASK"
        const val RESULT_DATA_QR_LINK_SOLAR = "RESULT_DATA_QR_LINK_SOLAR"
        fun newIntentForPairingDriver(context: Context): Intent {
            val intent = Intent(context, ScannerActivity::class.java)
            intent.putExtra(SCAN_TYPE, SCAN_FOR_PAIRING)
            return intent
        }

        fun newIntentForCheckingFleet(context: Context, data: DeliveryOrderModel): Intent {
            val intent = Intent(context, ScannerActivity::class.java)
            intent.putExtra(SCAN_TYPE, SCAN_FOR_CHECKING)
            intent.putExtra(DATA_DELIVERY_ORDER, data)
            return intent
        }

        fun newIntentForCheckingFleetMechanic(context: Context, data: MechanicTaskModel): Intent {
            val intent = Intent(context, ScannerActivity::class.java)
            intent.putExtra(SCAN_TYPE, SCAN_FOR_CHECKING)
            intent.putExtra(DATA_MECHANIC_TASK, data)
            return intent
        }

        fun newIntentForRefill(context: Context): Intent {
            val intent = Intent(context, ScannerActivity::class.java)
            intent.putExtra(SCAN_TYPE, SCAN_FOR_REFILL)
            return intent
        }
    }

    private lateinit var binding: ActivityScannerBinding

    private var scannedQr = ""
    private var scanType = ""
    private lateinit var codeScanner: CodeScanner
    private val viewModel: ScannerViewModel by viewModels()
    @Inject lateinit var userData: DataStore<UserDataModel>

    private val startPairingActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scanType = intent.getStringExtra(SCAN_TYPE) ?: ""

        initScanner()
        initActions()
        initObserver()
    }

    private fun initObserver() {
        viewModel.checkQr.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    if (success.data) {
                        if (scanType == SCAN_FOR_CHECKING) {
                            viewModel.checkPairing(scannedQr)
                        } else {
                            showSuccessScanDialog()
                        }
                    } else {
                        showFailedScanDialog(getString(R.string.message_fail_scan))
                    }
                }, onFailure = {
                    showFailedScanDialog(getString(R.string.message_fail_scan))
                }
            )
        }

        viewModel.checkPairing.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    if (success.data.isNotEmpty()) {
                        when (scanType) {
                            SCAN_FOR_REFILL -> {
                                setResult(Activity.RESULT_OK, intent.apply {
                                    putExtra(RESULT_DATA_QR_LINK_SOLAR, success.data.firstOrNull()?.qrLinkSolar ?: "")
                                })
                                finish()
                            }
                            SCAN_FOR_PAIRING -> {
                                startPairingActivityForResult.launch(PairingConfirmationActivity.newIntent(this@ScannerActivity, success.data[0]))
                            }
                            SCAN_FOR_CHECKING -> {
                                val currentTaskFleetId: DeliveryOrderModel = intent.getParcelableExtra(DATA_DELIVERY_ORDER)!!
                                if (success.data.first().id == currentTaskFleetId.idFleet) {
                                    showSuccessScanDialog()
                                } else {
                                    showFailedScanDialog(getString(R.string.message_fail_scan_for_pair))
                                }
                            }
                            else -> Unit
                        }
                    }
                }, onFailure = {
                    showFailedScanDialog(getString(R.string.message_fail_scan_for_pair))
                }
            )
        }

        viewModel.checkQrMaintenance.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    showSuccessScanDialog()
                }, onFailure = {
                    showFailedScanDialog(getString(R.string.message_fail_scan))
                }
            )
        }
    }

    private fun initScanner(){
        codeScanner = CodeScanner(this, binding.scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE) // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        codeScanner.startPreview()
    }

    private fun initActions(){
        binding.toolbarScanner.setOnNavigationClickListener {
            finish()
        }
        binding.btnGoToHome.setOnClickListener {
            finish()
//            DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_scan_success,
//                getString(R.string.title_success_scan), null, positiveButton = Pair(getString(R.string.button_continue)) {
//                    setResult(Activity.RESULT_OK)
//                    finish()
//                })
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

//        binding.btnFlipCamera.setOnClickListener {
//            if (this::codeScanner.isInitialized){
//                when (codeScanner.camera) {
//                    CodeScanner.CAMERA_BACK -> codeScanner.camera = CodeScanner.CAMERA_FRONT
//                    CodeScanner.CAMERA_FRONT -> codeScanner.camera = CodeScanner.CAMERA_BACK
//                    0 -> codeScanner.camera = CodeScanner.CAMERA_FRONT
//                    1 -> codeScanner.camera = CodeScanner.CAMERA_BACK
//                }
//            }
//        }

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //success scan
                vibrateDevice()
                scannedQr = it.toString()
                when (scanType) {
                    SCAN_FOR_PAIRING, SCAN_FOR_REFILL -> viewModel.checkPairing(it.toString())
                    SCAN_FOR_CHECKING -> {
                        runBlocking {
                            userData.data.first().roleType.run {
                                when (this) {
                                    UserRoleType.HEAD_MECHANIC, UserRoleType.MECHANIC -> {
                                        val mechanicTask: MechanicTaskModel? = intent.getParcelableExtra(DATA_MECHANIC_TASK)
                                        mechanicTask?.let { taskData ->
                                            viewModel.checkQrMechanicMaintenance(it.toString(), taskData.id)
                                        }
                                    }
                                    else -> {
                                        viewModel.checkQr(it.toString())
                                    }
                                }
                            }
                        }
                    }
                    else -> viewModel.checkQr(it.toString())
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                //error
                DialogUtils.showWhiteAlertDialog(
                    this,
                    getString(R.string.error),
                    it.message,
                    positiveButton = Pair(getString(R.string.ok)) {

                    }
                )
            }
        }
    }

    private fun vibrateDevice() {
        val vibrator: Vibrator? = ContextCompat.getSystemService(this@ScannerActivity, Vibrator::class.java)
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(100)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (this::codeScanner.isInitialized) codeScanner.releaseResources()
        super.onPause()
    }

    private fun showSuccessScanDialog() {
        DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_scan_success,
            getString(R.string.title_success_scan), null, positiveButton = Pair(getString(R.string.button_continue)) {
                setResult(Activity.RESULT_OK)
                finish()
            })
    }

    private fun showFailedScanDialog(message: String) {
        DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_scan_fail,
            getString(R.string.title_fail_scan), message, positiveButton = Pair(getString(R.string.button_retry)) {
                if (this::codeScanner.isInitialized) codeScanner.startPreview()
            })
    }
}