package com.automa.ui.shared.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.account.model.ProfileDriverModel
import com.automa.domain.account.model.ProfileMechanicModel
import com.automa.domain.account.usecase.ProfileDriverUseCase
import com.automa.domain.account.usecase.ProfileMechanicUseCase
import com.automa.domain.account.model.DriverSettingsModel
import com.automa.domain.auth.model.LoginModel
import com.automa.domain.account.usecase.GetDriverSettingsUseCase
import com.automa.domain.auth.usecase.LoginUseCase
import com.automa.domain.auth.usecase.RegisterMechanicUseCase
import com.automa.domain.common.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerMechanicUseCase: RegisterMechanicUseCase,
    private val profileMechanicUseCase: ProfileMechanicUseCase,
    private val profileDriverUseCase: ProfileDriverUseCase,
    private val driverSettingsUseCase: GetDriverSettingsUseCase
) : ViewModel() {
    private val _loginResult = MutableLiveData<ResultWrapper<LoginModel>>()
    val loginResult = _loginResult as LiveData<ResultWrapper<LoginModel>>

    private val _registerMechanic = MutableLiveData<ResultWrapper<HashMap<String, Any>>>()
    val registerMechanic = _registerMechanic as LiveData<ResultWrapper<HashMap<String, Any>>>

    private val _mechanicProfile = MutableLiveData<ResultWrapper<List<ProfileMechanicModel>>>()
    val mechanicProfile = _mechanicProfile as LiveData<ResultWrapper<List<ProfileMechanicModel>>>

    private val _driverProfile = MutableLiveData<ResultWrapper<List<ProfileDriverModel>>>()
    val driverProfile = _driverProfile as LiveData<ResultWrapper<List<ProfileDriverModel>>>

    private val _driverSettings = MutableLiveData<ResultWrapper<List<DriverSettingsModel>>>()
    val driverSettings = _driverSettings as LiveData<ResultWrapper<List<DriverSettingsModel>>>

    fun requestLogin(username: String, password: String) {
        _loginResult.value = ResultWrapper.loading()
        viewModelScope.launch {
            loginUseCase.addParams(username, password).execute().run(_loginResult::postValue)
        }
    }

    fun registerMechanic(
        nik: String,
        firstName: String,
        lastName: String,
        birthDate: String,
        joinDate: String,
        address: String,
        whatsappNumber: String,
        phoneNumber: String,
        email: String,
        username: String,
        password: String
    ) {
        _registerMechanic.value = ResultWrapper.loading()
        viewModelScope.launch {
            registerMechanicUseCase.addParams(
                nik, firstName, lastName, birthDate, joinDate, address,
                whatsappNumber, phoneNumber, email, username, password
            ).execute().run(_registerMechanic::postValue)
        }
    }

    fun getMechanicProfile(id: Int) {
        _mechanicProfile.value = ResultWrapper.loading()
        viewModelScope.launch {
            profileMechanicUseCase.addParams(id).execute().run(_mechanicProfile::postValue)
        }
    }

    fun getDriverProfile(id: Int) {
        _driverProfile.value = ResultWrapper.loading()
        viewModelScope.launch {
            profileDriverUseCase.addParams(id).execute().run(_driverProfile::postValue)
        }
    }

    fun getDriverSettings() {
        _driverSettings.value = ResultWrapper.loading()
        viewModelScope.launch {
            driverSettingsUseCase.execute().run(_driverSettings::postValue)
        }
    }
}