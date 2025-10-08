package com.automa.domain.qr.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.qr.QrRepository
import com.automa.domain.qr.model.CheckQrPairingModel
import javax.inject.Inject

class CheckQrMechanicMaintenanceUseCase @Inject constructor(
    private val repository: QrRepository
): BaseUseCase<List<CheckQrPairingModel>>() {
    private var token = ""
    private var idScheduledMaintenance = -1
    fun addParams(token: String, idScheduledMaintenance: Int) = apply {
        this.token = token
        this.idScheduledMaintenance = idScheduledMaintenance
    }

    override suspend fun execute(): ResultWrapper<List<CheckQrPairingModel>> {
        return repository.checkQrMechanicMaintenance(token, idScheduledMaintenance)
    }
}