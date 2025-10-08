package com.automa.data.mechanic_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.mechanic_task.model.MechanicTaskProofResponse
import com.automa.domain.mechanic_task.model.MechanicTaskProofModel

class MechanicTaskProofMapper: Mapper<List<MechanicTaskProofResponse>, List<MechanicTaskProofModel>> {
    override fun mapFromResponse(response: List<MechanicTaskProofResponse>): List<MechanicTaskProofModel> {
        return response.map {
            MechanicTaskProofModel(
                id = it.id.orDefault(),
                photoLink = it.photoLink.orDefault(),
                name = it.name.orDefault(),
                desc = it.desc.orDefault(),
                createdOn = it.createdOn.orDefault(),
                modifiedOn = it.modifiedOn.orDefault(),
                idScheduledMaintenanceMechanicTask = it.idScheduledMaintenanceMechanicTask.orDefault(),
                scheduledMaintenanceMechanicTaskName = it.scheduledMaintenanceMechanicTaskName.orDefault(),
                scheduledMaintenanceMechanicTaskDesc = it.scheduledMaintenanceMechanicTaskDesc.orDefault()
            )
        }
    }
}