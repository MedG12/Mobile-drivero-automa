package com.automa.domain.account.usecase

import com.automa.domain.account.AccountRepository
import com.automa.domain.account.model.CertificationMechanicModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class GetMechanicCertificationUseCase @Inject constructor(
    private val repository: AccountRepository
): BaseUseCase<List<CertificationMechanicModel>>() {
    private var idMechanic = -1

    fun addParams(idMechanic: Int) = apply {
        this.idMechanic = idMechanic
    }

    override suspend fun execute(): ResultWrapper<List<CertificationMechanicModel>> {
        return repository.getMechanicCertifications(idMechanic)
    }
}