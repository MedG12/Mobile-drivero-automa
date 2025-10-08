package com.automa.domain.mechanic_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import javax.inject.Inject

class HeadMechanicWorkshopTaskListUseCase @Inject constructor(
    private val repository: MechanicTaskRepository
): BaseUseCase<List<MechanicTaskModel>>() {

    override suspend fun execute(): ResultWrapper<List<MechanicTaskModel>> {
        return repository.getWorkshopTaskList()
    }
}