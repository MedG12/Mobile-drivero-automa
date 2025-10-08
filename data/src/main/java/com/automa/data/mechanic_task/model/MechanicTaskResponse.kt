package com.automa.data.mechanic_task.model


import com.google.gson.annotations.SerializedName

data class MechanicTaskResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("maintenance_number")
    val maintenanceNumber: String? = null,
    @SerializedName("maintenance_title")
    val maintenanceTitle: String? = null,
    @SerializedName("id_iterative_setting")
    val idIterativeSetting: Int? = null,
    @SerializedName("scheduled_datetime")
    val scheduledDatetime: String? = null,
    @SerializedName("realization_maintenance_datetime")
    val realizationMaintenanceDatetime: String? = null,
    @SerializedName("odometer_est")
    val odometerEst: Int? = null,
    @SerializedName("odometer_realization")
    val odometerRealization: Int? = null,
    @SerializedName("maintenance_desc")
    val maintenanceDesc: String? = null,
    @SerializedName("maintenance_est_cost")
    val maintenanceEstCost: String? = null,
    @SerializedName("maintenance_realization_cost")
    val maintenanceRealizationCost: String? = null,
    @SerializedName("progress_status")
    val progressStatus: Int? = null,
    @SerializedName("system_calc_est_cost")
    val systemCalcEstCost: String? = null,
    @SerializedName("progress_status_remark")
    val progressStatusRemark: String? = null,
    @SerializedName("created_on")
    val createdOn: String? = null,
    @SerializedName("modified_on")
    val modifiedOn: String? = null,
    @SerializedName("id_workshop")
    val idWorkshop: Int? = null,
    @SerializedName("workshop_name")
    val workshopName: String? = null,
    @SerializedName("workshop_address")
    val workshopAddress: String? = null,
    @SerializedName("workshop_latitude")
    val workshopLatitude: Double? = null,
    @SerializedName("workshop_longitude")
    val workshopLongitude: Double? = null,
    @SerializedName("workshop_main_phone_number")
    val workshopMainPhoneNumber: String? = null,
    @SerializedName("id_scheduled_maintenance_approval_type")
    val idScheduledMaintenanceApprovalType: Int? = null,
    @SerializedName("scheduled_maintenance_approval_type_name")
    val scheduledMaintenanceApprovalTypeName: String? = null,
    @SerializedName("id_fleet")
    val idFleet: Int? = null,
    @SerializedName("fleet_reg_number")
    val fleetRegNumber: String? = null,
    @SerializedName("fleet_reg_year")
    val fleetRegYear: String? = null,
    @SerializedName("workshop_proposed_schedule")
    val workshopProposedSchedule: String? = null,
    @SerializedName("duration")
    val duration: String? = null,
    @SerializedName("activity_type")
    val activityType: String? = null,
    @SerializedName("is_iterative")
    val isIterative: String? = null,
    @SerializedName("fleet_notes")
    val fleetNotes: String? = null,
    @SerializedName("mechanicTasks")
    val mechanicTasks: List<MechanicTaskItemResponse> ?= null
)

data class MechanicTaskItemResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("task_name")
    val taskName: String? = null,
    @SerializedName("desc")
    val desc: String ?= null,
    @SerializedName("id_scheduled_maintenance_mechanic")
    val idScheduledMechanic: Int,
    @SerializedName("scheduled_maintenance_scheduled_datetime")
    val scheduledDateTime: String,
    @SerializedName("mechanic_first_name")
    val mechanicFirstName: String,
    @SerializedName("mechanic_last_name")
    val mechanicLastName: String,
    @SerializedName("total_cost")
    val totalCost: Double,
    @SerializedName("total_duration")
    val totalDuration: Int
)