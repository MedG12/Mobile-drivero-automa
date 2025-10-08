package com.automa.domain.qr.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.qr.QrRepository
import javax.inject.Inject

class CheckQrUseCase @Inject constructor(
    private val repository: QrRepository
): BaseUseCase<Boolean>() {
    private var token = ""
    fun addParams(token: String) = apply {
        this.token = token
    }

    override suspend fun execute(): ResultWrapper<Boolean> {
        return repository.checkQr(token)
    }
}