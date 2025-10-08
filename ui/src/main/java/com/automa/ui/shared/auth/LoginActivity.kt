package com.automa.ui.shared.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.*
import com.automa.domain.ErrorModel
import com.automa.domain.account.model.ProfileDriverModel
import com.automa.domain.account.model.ProfileMechanicModel
import com.automa.domain.account.model.DriverSettingsModel
import com.automa.domain.auth.model.LoginModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityLoginBinding
import com.automa.ui.driver.home.HomeActivity
import com.automa.ui.live_monitoring.LiveMonitoringActivity
import com.automa.ui.mechanic.home.HomeMechanicActivity
import com.automa.ui.mechanic.register.RegisterMechanicActivity
import com.automa.ui.utils.checkIsNotEmpty
import com.automa.ui.utils.onTextValidation
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject lateinit var userData: DataStore<UserDataModel>

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initValidation()
        initObserver()

        binding.rgLoginType.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                binding.rbDriver.id -> {
                    binding.tvRegister.isGone = true
                }
                binding.rbMechanic.id -> {
                    binding.tvRegister.isGone = true
                }
            }
        }
        binding.rgLoginType.isGone = true

        binding.btnLogin.setOnClickListener {
            binding.tilUsername.checkIsNotEmpty(binding.edtUsername)
            binding.tilPassword.checkIsNotEmpty(binding.edtPassword)
            when {
                binding.tilUsername.error != null -> binding.edtUsername.requestFocus()
                binding.tilPassword.error != null -> binding.edtPassword.requestFocus()
                else -> {
                    authViewModel.requestLogin(binding.edtUsername.text.toString(), binding.edtPassword.text.toString())
                }
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterMechanicActivity::class.java))
        }
    }

    private fun initValidation() {
        binding.tilUsername.onTextValidation(binding.edtUsername)
        binding.tilPassword.onTextValidation(binding.edtPassword)
    }

    private var tryGetHardcodeProfile = false
    private fun initObserver() {
        authViewModel.driverSettings.observe(this) {
            it.handleResult(
                onSuccess = { result ->
                    if (result.data.isNotEmpty()) {
                        saveDriverSettings(result.data.first())
                    } else {
                        resetSavedData()
                        toast(this@LoginActivity, "Terjadi kesalahan, silahkan coba lagi nanti")
                    }
                }, onFailure = { error ->
                    resetSavedData()
                    showErrorMessage(error.errorData)
                }
            )
        }
        authViewModel.loginResult.observe(this) {
            it.handleResult(
                onSuccess = { result ->
                    saveLoginData(result.data)
                    when (result.data.idRoles) {
                        //Live Monitoring Accounts
                        1, 2, 3, 4 -> {
                            runBlocking {
                                userData.updateData { mData ->
                                    mData.copy(
                                        roleType = UserRoleType.LIVE_MONITORING
                                    )
                                }
                            }
                            moveToLiveMonitoring()
                        }
                        //Driver Accounts
                        8 -> {
                            authViewModel.getDriverProfile(result.data.userId)
                        }
                        //Mechanic Accounts (Regular & Head)
                        6, 7 -> {
                            authViewModel.getMechanicProfile(result.data.userId)
                        }
                        //Unknown
                        else -> {
                            resetSavedData()
                            toast(this@LoginActivity, "Unknown Role Type")
                        }
                    }
//                    if (result.data.roleName.equals("Driver", true)) {
//                        authViewModel.getDriverProfile(result.data.userId)
//                    } else {
//                        authViewModel.getMechanicProfile(result.data.userId)
//                    }
                },
                onFailure = { error ->
                    resetSavedData()
                    showErrorMessage(error.errorData)
                }
            )
        }

        authViewModel.driverProfile.observe(this) {
            it.handleResult(
                onSuccess = { result ->
                    if (result.data.isNotEmpty()) {
//                        saveDriverProfile(result.data.find { item -> item.id == 422 } ?: result.data.first())
                        saveDriverProfile(result.data.first())
                        authViewModel.getDriverSettings()
                    } else {
//                        if (!tryGetHardcodeProfile) {
//                            authViewModel.getDriverProfile(422)
//                            tryGetHardcodeProfile = true
//                        }
                        resetSavedData()
                    }
                }, onFailure = { error ->
                    resetSavedData()
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        authViewModel.mechanicProfile.observe(this) {
            it.handleResult(
                onSuccess = { result ->
                    if (result.data.isNotEmpty()) {
                        saveMechanicProfile(result.data.first())
                    } else {
//                        if (!tryGetHardcodeProfile) {
//                            authViewModel.getMechanicProfile(21)
//                            tryGetHardcodeProfile = true
//                        }
                        resetSavedData()
                    }
                }, onFailure = { error ->
                    resetSavedData()
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }
    }

    private fun showErrorMessage(error: ErrorModel) {
        if (error.statusCode in 400..401) {
            binding.tilPassword.error = getString(R.string.message_error_credentials)
        } else {
            toast(this, "Error: ${error.message}")
        }
    }

    private fun moveToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun moveToHomeMechanic() {
        startActivity(Intent(this, HomeMechanicActivity::class.java))
        finish()
    }

    private fun moveToLiveMonitoring() {
        startActivity(Intent(this, LiveMonitoringActivity::class.java))
        finish()
    }

    private fun saveMechanicProfile(data: ProfileMechanicModel) {
        val role = when (data.idMechanicPosition) {
            1 -> UserRoleType.HEAD_MECHANIC
            2 -> UserRoleType.MECHANIC
            else -> UserRoleType.NONE
        }
        lifecycleScope.launch {
            userData.updateData {
                it.copy(
                    roleType = role,
                    mechanicProfile = ProfileMechanicData(
                        id = data.id,
                        nik = data.nik,
                        idCompany = data.idCompany,
                        userId = data.userId,
                        firstName = data.firstName,
                        lastName = data.lastName,
                        birthDate = data.birthDate,
                        joinDate = data.joinDate,
                        leaveDate = data.leaveDate,
                        address = data.address,
                        isActive = data.isActive,
                        unactiveReason = data.unactiveReason,
                        photoLink = data.photoLink,
                        whatsappNumber = data.whatsappNumber,
                        phoneNumber = data.phoneNumber,
                        email = data.email,
                        experienceRemark = data.experienceRemark,
                        idMechanicPosition = data.idMechanicPosition,
                        mechanicPositionName = data.mechanicPositionName
                    )
                )
            }
            moveToHomeMechanic()
        }
    }

    private fun saveLoginData(data: LoginModel) {
        runBlocking {
            userData.updateData {
                it.copy(
                    token = data.token,
                    roleType = UserRoleType.NONE,
                    loginData = LoginData(
                        userId = data.userId,
                        name = data.name,
                        email = data.email,
                        emailVerified = data.emailVerified,
                        idCompany = data.idCompany,
                        companyName = data.companyName,
                        companyPhone = data.companyPhone,
                        idRoles = data.idRoles,
                        roleName = data.roleName,
                        subsData = LoginSubsData(
                            idPlan = data.subsData.idPlan,
                            planName = data.subsData.planName,
                            totalFleet = data.subsData.totalFleet,
                            userLevel = data.subsData.userLevel,
                            userAmount = data.subsData.userAmount,
                            managerApproval = data.subsData.managerApproval,
                            securityApproval = data.subsData.securityApproval,
                            totalDriver = data.subsData.totalDriver,
                            totalDriverAss = data.subsData.totalDriverAss,
                            customer = data.subsData.customer,
                            fleetHistory = data.subsData.fleetHistory,
                            advanceSchedule = data.subsData.advanceSchedule,
                            ddor = data.subsData.ddor,
                            utilityReport = data.subsData.utilityReport,
                            disconnectedReport = data.subsData.disconnectedReport,
                            sharedTrack = data.subsData.sharedTrack
                        )
                    )
                )
            }
        }
    }

    private fun saveDriverProfile(data: ProfileDriverModel) {
        lifecycleScope.launch {
            userData.updateData {
                it.copy(
                    roleType = UserRoleType.DRIVER,
                    driverProfile = ProfileDriverData(
                        id = data.id,
                        idCompany = data.idCompany,
                        idImageLog = data.idImageLog,
                        linkImage = data.linkImage,
                        descImage = data.descImage,
                        idImageLogLic = data.idImageLogLic,
                        linkImageLic = data.linkImageLic,
                        descImageLic = data.descImageLic,
                        name = data.name,
                        telp = data.telp,
                        ktp = data.ktp,
                        birthDate = data.birthDate,
                        idDriverLic = data.idDriverLic,
                        licType = data.licType,
                        licNumber = data.licNumber,
                        expDate = data.expDate,
                        userId = data.userId,
                        createdOn = data.createdOn
                    )
                )
            }
        }
    }

    private fun saveDriverSettings(data: DriverSettingsModel) {
        lifecycleScope.launch {
            userData.updateData {
                it.copy(
                    driverProfile = it.driverProfile?.copy(
                        statusCopyDo = data.status == 1
                    )
                )
            }
            moveToHome()
        }
    }

    private fun resetSavedData() {
        runBlocking {
            userData.updateData {
                UserDataModel()
            }
        }
    }
}