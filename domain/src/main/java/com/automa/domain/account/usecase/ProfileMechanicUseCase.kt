package com.automa.domain.account.usecase

import com.automa.domain.account.AccountRepository
import com.automa.domain.account.model.ProfileMechanicModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class ProfileMechanicUseCase @Inject constructor(
    private val repository: AccountRepository
): BaseUseCase<List<ProfileMechanicModel>>() {
    private var id = -1

    fun addParams(id: Int) = apply {
        this.id = id
    }

    override suspend fun execute(): ResultWrapper<List<ProfileMechanicModel>> {
        return repository.getMechanicProfile(id)
    }
}