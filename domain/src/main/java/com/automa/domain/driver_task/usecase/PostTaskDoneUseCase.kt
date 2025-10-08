package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import javax.inject.Inject

class PostTaskDoneUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<Int>() {
    private var params = mapOf<String, Any>()

    fun addParams(id: Int) = apply {
        params = mapOf(
            ID_DO to id
        )
    }

    override suspend fun execute(): ResultWrapper<Int> {
        return repository.postTaskDone(params[ID_DO] as Int)
    }

    companion object {
        private const val ID_DO = "ID_DO"
    }
}