package com.automa.domain.mechanic_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import com.automa.domain.mechanic_task.model.MechanicTaskProofModel
import javax.inject.Inject

class MechanicGetListTaskProofBeforeUseCase @Inject constructor(
    private val repository: MechanicTaskRepository
): BaseUseCase<List<MechanicTaskProofModel>>() {
    private var taskId: Int = -1

    fun addParams(id: Int) = apply {
        this.taskId = id
    }

    override suspend fun execute(): ResultWrapper<List<MechanicTaskProofModel>> {
        return repository.getListTaskProofBefore(taskId)
    }
}