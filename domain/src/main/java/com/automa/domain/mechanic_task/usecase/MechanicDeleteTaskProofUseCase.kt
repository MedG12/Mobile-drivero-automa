package com.automa.domain.mechanic_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import javax.inject.Inject

class MechanicDeleteTaskProofUseCase @Inject constructor(
    private val repository: MechanicTaskRepository
): BaseUseCase<String>() {
    private var id = -1
    fun addParams(id: Int) = apply {
        this.id = id
    }
    override suspend fun execute(): ResultWrapper<String> {
        return repository.deleteTaskProof(id)
    }
}