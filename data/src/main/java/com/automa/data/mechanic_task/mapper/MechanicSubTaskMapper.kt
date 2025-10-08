package com.automa.data.mechanic_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.mechanic_task.model.MechanicSubTaskResponse
import com.automa.domain.mechanic_task.model.MechanicSubTaskModel

class MechanicSubTaskMapper: Mapper<List<MechanicSubTaskResponse>, List<MechanicSubTaskModel>> {
    override fun mapFromResponse(response: List<MechanicSubTaskResponse>): List<MechanicSubTaskModel> {
        return response.map {
            MechanicSubTaskModel(
                id = it.id.orDefault(),
                cost = it.cost.orDefault(),
                taskName = it.taskName.orDefault(),
                desc = it.desc.orDefault(),
                createdOn = it.createdOn.orDefault(),
                modifiedOn = it.modifiedOn.orDefault(),
                idScheduledMaintenanceMechanicTask = it.idScheduledMaintenanceMechanicTask.orDefault(),
                scheduledMaintenanceMechanicTaskName = it.scheduledMaintenanceMechanicTaskName.orDefault(),
                scheduledMaintenanceMechanicTaskDesc = it.scheduledMaintenanceMechanicTaskDesc.orDefault(),
                approval = it.approval.orDefault(),
                duration = it.duration.orDefault()
            )
        }
    }
}