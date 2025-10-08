package com.automa.domain.mechanic_task.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MechanicTaskModel(
    val id: Int,
    val maintenanceNumber: String,
    val maintenanceTitle: String,
    val idIterativeSetting: Int,
    val scheduledDatetime: String,
    val realizationMaintenanceDatetime: String,
    val odometerEst: Int,
    val odometerRealization: Int,
    val maintenanceDesc: String,
    val maintenanceEstCost: String,
    val maintenanceRealizationCost: String,
    val progressStatus: Int,
    val systemCalcEstCost: String,
    val progressStatusRemark: String,
    val createdOn: String,
    val modifiedOn: String,
    val idWorkshop: Int,
    val workshopName: String,
    val workshopAddress: String,
    val workshopLatitude: Double,
    val workshopLongitude: Double,
    val workshopMainPhoneNumber: String,
    val idScheduledMaintenanceApprovalType: Int,
    val scheduledMaintenanceApprovalTypeName: String,
    val idFleet: Int,
    val fleetRegNumber: String,
    val fleetRegYear: String,
    val workshopProposedSchedule: String,
    val duration: String,
    val activityType: String,
    val isIterative: String,
    val fleetNotes: String,
    val tasks: List<MechanicTaskItemModel>
): Parcelable

@Parcelize
data class MechanicTaskItemModel(
    val id: Int,
    val taskName: String,
    val desc: String,
    val idScheduledMechanic: Int,
    val scheduledDateTime: String,
    val mechanicFirstName: String,
    val mechanicLastName: String,
    val totalCost: Double,
    val totalDuration: Int,
    val fleetPlate: String
): Parcelable