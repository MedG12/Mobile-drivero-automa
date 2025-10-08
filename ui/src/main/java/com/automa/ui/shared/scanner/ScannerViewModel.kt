package com.automa.ui.shared.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.common.ResultWrapper
import com.automa.domain.qr.model.CheckQrPairingModel
import com.automa.domain.qr.usecase.CheckQrFleetUseCase
import com.automa.domain.qr.usecase.CheckQrMechanicMaintenanceUseCase
import com.automa.domain.qr.usecase.CheckQrUseCase
import com.automa.domain.qr.usecase.FleetPairingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val checkQrUseCase: CheckQrUseCase,
    private val checkQrFleetUseCase: CheckQrFleetUseCase,
    private val fleetPairingUseCase: FleetPairingUseCase,
    private val checkQrMechanicMaintenanceUseCase: CheckQrMechanicMaintenanceUseCase
): ViewModel() {
    private val _checkQr = MutableLiveData<ResultWrapper<Boolean>>()
    val checkQr = _checkQr as LiveData<ResultWrapper<Boolean>>

    private val _checkPairing = MutableLiveData<ResultWrapper<List<CheckQrPairingModel>>>()
    val checkPairing = _checkPairing as LiveData<ResultWrapper<List<CheckQrPairingModel>>>

    private val _fleetPairing = MutableLiveData<ResultWrapper<List<CheckQrPairingModel>>>()
    val fleetPairing = _fleetPairing as LiveData<ResultWrapper<List<CheckQrPairingModel>>>

    private val _checkQrMaintenance = MutableLiveData<ResultWrapper<List<CheckQrPairingModel>>>()
    val checkQrMaintenance = _checkQrMaintenance as LiveData<ResultWrapper<List<CheckQrPairingModel>>>

    fun checkQr(token: String) {
        viewModelScope.launch {
            _checkQr.value = ResultWrapper.loading()
            checkQrUseCase.addParams(token).execute().run(_checkQr::postValue)
        }
    }

    fun checkPairing(token: String) {
        viewModelScope.launch {
            _checkPairing.value = ResultWrapper.loading()
            checkQrFleetUseCase.addParams(token).execute().run(_checkPairing::postValue)
        }
    }

    fun checkFleetPairing(idDriver: Int, idFleet: Int) {
        viewModelScope.launch {
            _fleetPairing.value = ResultWrapper.loading()
            fleetPairingUseCase.addParams(idDriver, idFleet).execute().run(_fleetPairing::postValue)
        }
    }

    fun checkQrMechanicMaintenance(token: String, idScheduledMaintenance: Int) {
        viewModelScope.launch {
            _checkQrMaintenance.value = ResultWrapper.loading()
            checkQrMechanicMaintenanceUseCase.addParams(token, idScheduledMaintenance).execute().run(_checkQrMaintenance::postValue)
        }
    }
}