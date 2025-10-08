package com.automa.ui.driver.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.ui.R
import com.automa.ui.databinding.ActivityAccountEditBinding
import com.automa.ui.utils.DateUtils
import com.automa.ui.utils.DialogUtils
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AccountEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountEditBinding

    @Inject lateinit var userData: DataStore<UserDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAccount.setOnNavigationClickListener {
            finish()
        }

        binding.btnCancelPair.setOnClickListener {
            DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_cancel_pairing,
                getString(R.string.title_dialog_cancel_pair),
                getString(R.string.message_dialog_cancel_pair),
                positiveButton = Pair(getString(R.string.button_back)) {},
                negativeButton = Pair(getString(R.string.button_cancel_pair)) {
                    runBlocking {
                        userData.updateData {
                            it.copy(
                                driverProfile = it.driverProfile?.copy(
                                    pairedFleet = null
                                )
                            )
                        }
                    }
                })
        }

        lifecycleScope.launchWhenCreated {
            userData.data.collect {
                it.driverProfile?.let { profile ->
                    Glide.with(this@AccountEditActivity)
                        .load(profile.linkImage)
                        .into(binding.ivPhoto)
                    binding.tvUsername.text = profile.name.lowercase().capitalize(Locale.ROOT)
                    binding.tvPhoneNumber.text = profile.telp
                    binding.tvIdNumber.text = profile.ktp
                    binding.tvLicenseType.text = profile.licType
                    binding.tvLicenseNumber.text = profile.licNumber
                    val dateToShow = try {
                        DateUtils.formatDate(profile.birthDate, DateUtils.DEFAULT_DATE_FORMAT, "yyyy-MM-dd")
                    } catch (e: Exception) { profile.birthDate }
                    binding.tvDob.text = dateToShow
                    profile.pairedFleet?.let { vehicleData ->
                        binding.clLinkedFleet.isVisible = true
                        binding.tvLabelFleetPlate.text = vehicleData.regNumber
                        binding.tvFleetRegistrationNumber.text = vehicleData.regNumber
                        binding.tvFleetBrand.text = vehicleData.carBrands
                        binding.tvFleetVehicleType.text = vehicleData.carType
                        binding.tvFleetRegistrationYear.text = vehicleData.regYear.toString()
                        binding.tvFleetManufacturingYear.text = vehicleData.manufactureYear.toString()
                        binding.tvFleetVehicleIdRegistered.text = vehicleData.vehicleIdNumber
                        binding.tvFleetEngineNumber.text = vehicleData.engineNumber
                        binding.tvFleetCylinderCapacity.text = vehicleData.cylCap.toString()
                    } ?: run {
                        binding.clLinkedFleet.isVisible = false
                    }
                }
            }
        }
    }
}