package com.automa.ui.mechanic.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.account.model.CertificationMechanicModel
import com.automa.domain.account.usecase.DeleteMechanicCertificationUseCase
import com.automa.domain.account.usecase.GetMechanicCertificationUseCase
import com.automa.domain.account.usecase.InputMechanicCertificationUseCase
import com.automa.domain.account.usecase.UpdateMechanicCertificationUseCase
import com.automa.domain.common.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountMechanicViewModel @Inject constructor(
    private val getMechanicCertificationUseCase: GetMechanicCertificationUseCase,
    private val inputMechanicCertificationUseCase: InputMechanicCertificationUseCase,
    private val deleteMechanicCertificationUseCase: DeleteMechanicCertificationUseCase,
    private val updateMechanicCertificationUseCase: UpdateMechanicCertificationUseCase
): ViewModel() {
    private val _listCertifications = MutableLiveData<ResultWrapper<List<CertificationMechanicModel>>>()
    val listCertifications = _listCertifications as LiveData<ResultWrapper<List<CertificationMechanicModel>>>

    private val _inputCertification = MutableLiveData<ResultWrapper<String>>()
    val inputCertification = _inputCertification as LiveData<ResultWrapper<String>>

    private val _deleteCertification = MutableLiveData<ResultWrapper<String>>()
    val deleteCertification = _deleteCertification as LiveData<ResultWrapper<String>>

    private val _updateCertification = MutableLiveData<ResultWrapper<String>>()
    val updateCertification = _updateCertification as LiveData<ResultWrapper<String>>

    fun getCertifications(idMechanic: Int) {
        viewModelScope.launch {
            _listCertifications.value = ResultWrapper.loading()
            getMechanicCertificationUseCase.addParams(idMechanic).execute().run(_listCertifications::postValue)
        }
    }

    fun inputCertification(idMechanic: Int, name: String, desc: String, certDate: String, expDate: String, level: String) {
        viewModelScope.launch {
            _inputCertification.value = ResultWrapper.loading()
            inputMechanicCertificationUseCase.addParams(idMechanic, name, desc, certDate, expDate, level).execute().run(_inputCertification::postValue)
        }
    }

    fun deleteCertification(id: Int) {
        viewModelScope.launch {
            _deleteCertification.value = ResultWrapper.loading()
            deleteMechanicCertificationUseCase.addParams(id).execute().run(_deleteCertification::postValue)
        }
    }

    fun updateCertification(id: Int, idMechanic: Int, name: String, desc: String, certDate: String, expDate: String, level: String) {
        viewModelScope.launch {
            _updateCertification.value = ResultWrapper.loading()
            updateMechanicCertificationUseCase.addParams(id, idMechanic, name, desc, certDate, expDate, level).execute().run(_updateCertification::postValue)
        }
    }
}