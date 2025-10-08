package com.automa.domain.mechanic_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import javax.inject.Inject

class MechanicTaskByIdUseCase @Inject constructor(
    private val repository: MechanicTaskRepository
): BaseUseCase<List<MechanicTaskModel>>() {
    private var idMechanic = -1
    private var idTask = -1
    fun addParams(idMechanic: Int, idTask: Int) = apply {
        this.idMechanic = idMechanic
        this.idTask = idTask
    }

    override suspend fun execute(): ResultWrapper<List<MechanicTaskModel>> {
        return repository.getMechanicTaskById(idMechanic, idTask)
    }
}