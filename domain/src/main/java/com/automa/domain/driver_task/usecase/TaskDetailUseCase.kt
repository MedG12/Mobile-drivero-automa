package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.TaskDetailModel
import javax.inject.Inject

class TaskDetailUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<TaskDetailModel>() {
    private var params = mapOf<String, Any>()

    fun addParams(id: Int) = apply {
        params = mapOf(
            ID_DELIVERY_ORDER to id
        )
    }

    override suspend fun execute(): ResultWrapper<TaskDetailModel> {
        return repository.getTaskDetail(params[ID_DELIVERY_ORDER] as Int)
    }

    companion object {
        private const val ID_DELIVERY_ORDER = "ID"
    }
}