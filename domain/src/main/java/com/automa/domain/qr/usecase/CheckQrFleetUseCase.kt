package com.automa.domain.qr.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.qr.QrRepository
import com.automa.domain.qr.model.CheckQrPairingModel
import javax.inject.Inject

class CheckQrFleetUseCase @Inject constructor(
    private val repository: QrRepository
): BaseUseCase<List<CheckQrPairingModel>>() {
    private var token: String = ""
    fun addParams(token: String) = apply {
        this.token = token
    }

    override suspend fun execute(): ResultWrapper<List<CheckQrPairingModel>> {
        return repository.checkQrFleet(token)
    }
}