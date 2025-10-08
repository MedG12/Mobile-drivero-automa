package com.automa.domain.mechanic_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import javax.inject.Inject

class MechanicWorkshopTaskListUseCase @Inject constructor(
    private val repository: MechanicTaskRepository
): BaseUseCase<List<MechanicTaskModel>>() {
    private var id = -1
    fun addParams(id: Int) = apply {
        this.id = id
    }

    override suspend fun execute(): ResultWrapper<List<MechanicTaskModel>> {
        return repository.getWorkshopTaskList(id)
    }
}