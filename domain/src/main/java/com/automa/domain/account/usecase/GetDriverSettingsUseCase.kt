package com.automa.domain.account.usecase

import com.automa.domain.account.AccountRepository
import com.automa.domain.account.model.DriverSettingsModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class GetDriverSettingsUseCase @Inject constructor(
    private val repository: AccountRepository
): BaseUseCase<List<DriverSettingsModel>>() {
    override suspend fun execute(): ResultWrapper<List<DriverSettingsModel>> {
        return repository.getDriverSettings()
    }
}