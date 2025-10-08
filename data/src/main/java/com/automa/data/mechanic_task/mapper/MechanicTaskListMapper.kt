package com.automa.data.mechanic_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.mechanic_task.model.MechanicTaskResponse
import com.automa.domain.mechanic_task.model.MechanicTaskItemModel
import com.automa.domain.mechanic_task.model.MechanicTaskModel

class MechanicTaskListMapper: Mapper<List<MechanicTaskResponse>, List<MechanicTaskModel>> {
    override fun mapFromResponse(response: List<MechanicTaskResponse>): List<MechanicTaskModel> {
        return response.map { item ->
            MechanicTaskModel(
                id = item.id.orDefault(),
                maintenanceNumber = item.maintenanceNumber.orDefault(),
                maintenanceTitle = item.maintenanceTitle.orDefault(),
                idIterativeSetting = item.idIterativeSetting.orDefault(),
                scheduledDatetime = item.scheduledDatetime.orDefault(),
                realizationMaintenanceDatetime = item.realizationMaintenanceDatetime.orDefault(),
                odometerEst = item.odometerEst.orDefault(),
                odometerRealization = item.odometerRealization.orDefault(),
                maintenanceDesc = item.maintenanceDesc.orDefault(),
                maintenanceEstCost = item.maintenanceEstCost.orDefault(),
                maintenanceRealizationCost = item.maintenanceRealizationCost.orDefault(),
                progressStatus = item.progressStatus.orDefault(),
                systemCalcEstCost = item.systemCalcEstCost.orDefault(),
                progressStatusRemark = item.progressStatusRemark.orDefault(),
                createdOn = item.createdOn.orDefault(),
                modifiedOn = item.modifiedOn.orDefault(),
                idWorkshop = item.idWorkshop.orDefault(),
                workshopName = item.workshopName.orDefault(),
                workshopAddress = item.workshopAddress.orDefault(),
                workshopLatitude = item.workshopLatitude.orDefault(),
                workshopLongitude = item.workshopLongitude.orDefault(),
                workshopMainPhoneNumber = item.workshopMainPhoneNumber.orDefault(),
                idScheduledMaintenanceApprovalType = item.idScheduledMaintenanceApprovalType.orDefault(),
                scheduledMaintenanceApprovalTypeName = item.scheduledMaintenanceApprovalTypeName.orDefault(),
                idFleet = item.idFleet.orDefault(),
                fleetRegNumber = item.fleetRegNumber.orDefault(),
                fleetRegYear = item.fleetRegYear.orDefault(),
                workshopProposedSchedule = item.workshopProposedSchedule.orDefault(),
                duration = item.duration.orDefault(),
                activityType = item.activityType.orDefault(),
                isIterative = item.isIterative.orDefault(),
                fleetNotes = item.fleetNotes.orDefault(),
                tasks = item.mechanicTasks?.map {
                    MechanicTaskItemModel(
                        id = it.id.orDefault(),
                        taskName = it.taskName.orDefault(),
                        desc = it.desc.orDefault(),
                        idScheduledMechanic = it.idScheduledMechanic.orDefault(),
                        scheduledDateTime = it.scheduledDateTime.orDefault(),
                        mechanicFirstName = it.mechanicFirstName.orDefault(),
                        mechanicLastName = it.mechanicLastName.orDefault(),
                        totalCost = it.totalCost.orDefault(),
                        totalDuration = it.totalDuration.orDefault(),
                        fleetPlate = item.fleetRegNumber ?: item.fleetNotes ?: ""
                    )
                } ?: emptyList()
            )
        }
    }
}