package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.MasterDeliveryOrderCategoryModel
import javax.inject.Inject

class MasterDeliveryCategoryUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<List<MasterDeliveryOrderCategoryModel>>() {
    override suspend fun execute(): ResultWrapper<List<MasterDeliveryOrderCategoryModel>> {
        return repository.getMasterDeliveryOrderCategory()
    }
}