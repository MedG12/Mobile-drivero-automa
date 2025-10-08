package com.automa.ui.driver.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import com.automa.datastore.user_data.PairedFleet
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.qr.model.CheckQrPairingModel
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityPairingConfirmationBinding
import com.automa.ui.shared.scanner.ScannerViewModel
import com.automa.ui.utils.DateUtils
import com.automa.ui.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PairingConfirmationActivity : BaseActivity() {
    companion object {
        private const val VEHICLE_DATA = "DATA"
        fun newIntent(context: Context, data: CheckQrPairingModel): Intent {
            val intent = Intent(context, PairingConfirmationActivity::class.java)
            intent.putExtra(VEHICLE_DATA, data)
            return intent
        }
    }

    @Inject lateinit var userData: DataStore<UserDataModel>
    private lateinit var binding: ActivityPairingConfirmationBinding
    private lateinit var vehicleData: CheckQrPairingModel
    private val viewModel: ScannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPairingConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initActions()
        initObservable()
    }

    private fun initObservable() {
        viewModel.fleetPairing.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    if (success.data.isNotEmpty()) {
                        savePairingFleet()
                    } else {
                        showErrorPairingFleet()
                    }
                }, onFailure = {
                    showErrorPairingFleet()
                }
            )
        }
    }

    private fun initActions() {
        binding.toolbarPairingConfirmation.setOnNavigationClickListener { finish() }

        val idDriver = runBlocking { userData.data.first().driverProfile?.id } ?: -1
        val idFleet = vehicleData.id
        binding.btnPairFleet.setOnClickListener {
            viewModel.checkFleetPairing(idDriver, idFleet)
        }
    }

    private fun initUi() {
        vehicleData = intent.getParcelableExtra(VEHICLE_DATA)!!
        binding.tvLabelFleetPlate.text = vehicleData.regNumber
        binding.tvFleetRegistrationNumber.text = vehicleData.regNumber
        binding.tvFleetBrand.text = vehicleData.carBrands
        binding.tvFleetVehicleType.text = vehicleData.carType
        binding.tvFleetRegistrationYear.text = vehicleData.regYear.toString()
        binding.tvFleetManufacturingYear.text = vehicleData.manufactureYear.toString()
        binding.tvFleetVehicleIdRegistered.text = vehicleData.vehicleIdNumber
        binding.tvFleetEngineNumber.text = vehicleData.engineNumber
        binding.tvFleetCylinderCapacity.text = vehicleData.cylCap.toString()

        runBlocking {
            userData.data.first().run {
                binding.tvDriverName.text = this.driverProfile?.name
                binding.tvIdNumber.text = this.driverProfile?.ktp
                binding.tvDob.text = DateUtils.formatDate(this.driverProfile?.birthDate ?: "", DateUtils.DEFAULT_DATE_FORMAT, "d MMMM yyyy")
            }
        }
    }

    private fun savePairingFleet() {
        runBlocking {
            userData.updateData {
                it.copy(
                    driverProfile = it.driverProfile?.copy(
                        pairedFleet = PairedFleet(
                            id = vehicleData.id,
                            idCompany = vehicleData.idCompany,
                            company = vehicleData.company,
                            idCarGeneralType = vehicleData.idCarGeneralType,
                            carGeneralType = vehicleData.carGeneralType,
                            idCarBrands = vehicleData.idCarBrands,
                            carBrands = vehicleData.carBrands,
                            idCarType = vehicleData.idCarType,
                            carType = vehicleData.carType,
                            regNumber = vehicleData.regNumber,
                            regNumberWithDoor = vehicleData.regNumberWithDoor,
                            regYear = vehicleData.regYear,
                            manufactureYear = vehicleData.manufactureYear,
                            cylCap = vehicleData.cylCap,
                            vehicleIdNumber = vehicleData.vehicleIdNumber,
                            engineNumber = vehicleData.engineNumber,
                            active = vehicleData.active
                        )
                    )
                )
            }
        }
        DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_scan_success,
            getString(R.string.title_success_pair_fleet), getString(R.string.message_success_pair_fleet), positiveButton = Pair(getString(R.string.button_close)) {
                setResult(Activity.RESULT_OK)
                finish()
            })
    }

    private fun showErrorPairingFleet() {
        DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_scan_fail,
            getString(R.string.title_fail_pair_fleet), getString(R.string.message_fail_pair_fleet), positiveButton = Pair(getString(R.string.button_redo_process)) {
                finish()
            })
    }
}