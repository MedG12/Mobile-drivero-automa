package com.automa.domain.qr.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.qr.QrRepository
import com.automa.domain.qr.model.CheckQrPairingModel
import javax.inject.Inject

class FleetPairingUseCase @Inject constructor(
    private val repository: QrRepository
): BaseUseCase<List<CheckQrPairingModel>>() {
    private var idDriver: Int = -1
    private var idFleet: Int = -1
    fun addParams(idDriver: Int, idFleet: Int) = apply {
        this.idDriver = idDriver
        this.idFleet = idFleet
    }

    override suspend fun execute(): ResultWrapper<List<CheckQrPairingModel>> {
        return repository.checkQrFleetPairing(idDriver, idFleet)
    }
}