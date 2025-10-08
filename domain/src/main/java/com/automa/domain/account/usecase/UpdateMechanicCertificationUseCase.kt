package com.automa.domain.account.usecase

import com.automa.domain.account.AccountRepository
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class UpdateMechanicCertificationUseCase @Inject constructor(
    private val repository: AccountRepository
): BaseUseCase<String>() {
    private var id = -1
    private var idMechanic = -1
    private var name = ""
    private var desc = ""
    private var certDate = ""
    private var expDate = ""
    private var level = ""

    fun addParams(id: Int, idMechanic: Int, name: String, desc: String, certDate: String, expDate: String, level: String) = apply {
        this.id = id
        this.idMechanic = idMechanic
        this.name = name
        this.desc = desc
        this.certDate = certDate
        this.expDate = expDate
        this.level = level
    }

    override suspend fun execute(): ResultWrapper<String> {
        return repository.updateMechanicCertification(id, idMechanic, name, desc, certDate, expDate, level)
    }
}