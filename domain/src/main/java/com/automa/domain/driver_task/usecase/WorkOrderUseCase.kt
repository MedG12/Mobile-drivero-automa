package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.WorkOrderModel
import javax.inject.Inject

class WorkOrderUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<WorkOrderModel>() {
    private var params = mapOf<String, Any>()

    fun addParams(id: Int) = apply {
        params = mapOf(
            ID_DELIVERY_ORDER to id
        )
    }

    override suspend fun execute(): ResultWrapper<WorkOrderModel> {
        return repository.getWorkOrderList(params[ID_DELIVERY_ORDER] as Int)
    }

    companion object {
        private const val ID_DELIVERY_ORDER = "ID"
    }
}