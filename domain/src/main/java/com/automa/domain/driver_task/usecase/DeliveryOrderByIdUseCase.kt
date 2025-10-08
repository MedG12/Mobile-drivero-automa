package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.DeliveryOrderModel
import javax.inject.Inject

class DeliveryOrderByIdUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<List<DeliveryOrderModel>>() {
    private var id = -1

    fun addParams(id: Int) = apply {
        this.id = id
    }

    override suspend fun execute(): ResultWrapper<List<DeliveryOrderModel>> {
        return repository.getDeliveryOrderListById(id)
    }
}