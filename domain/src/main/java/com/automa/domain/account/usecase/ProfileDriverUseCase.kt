package com.automa.domain.account.usecase

import com.automa.domain.account.AccountRepository
import com.automa.domain.account.model.ProfileDriverModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class ProfileDriverUseCase @Inject constructor(
    private val repository: AccountRepository
): BaseUseCase<List<ProfileDriverModel>>() {
    private var id = -1

    fun addParams(id: Int) = apply {
        this.id = id
    }

    override suspend fun execute(): ResultWrapper<List<ProfileDriverModel>> {
        return repository.getDriverProfile(id)
    }
}