package com.automa.domain.mechanic_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import javax.inject.Inject

class MechanicUpdateTaskDetailUseCase @Inject constructor(
    private val repository: MechanicTaskRepository
): BaseUseCase<String>() {
    private var id = -1
    private var idTask = -1
    private var taskName = ""
    private var desc = ""
    private var cost = 0L
    private var duration = 0

    fun addParams(idTask: Int, id: Int, taskName: String, desc: String, cost: Long, duration: Int) = apply {
        this.idTask = idTask
        this.id = id
        this.taskName = taskName
        this.desc = desc
        this.cost = cost
        this.duration = duration
    }

    override suspend fun execute(): ResultWrapper<String> {
        return repository.updateTaskDetail(idTask, id, taskName, desc, cost, duration)
    }
}