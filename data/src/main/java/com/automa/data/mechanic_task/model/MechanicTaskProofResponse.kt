package com.automa.data.mechanic_task.model


import com.google.gson.annotations.SerializedName

data class MechanicTaskProofResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("photo_link")
    val photoLink: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("desc")
    val desc: String? = null,
    @SerializedName("created_on")
    val createdOn: String? = null,
    @SerializedName("modified_on")
    val modifiedOn: String? = null,
    @SerializedName("id_scheduled_maintenance_mechanic_task")
    val idScheduledMaintenanceMechanicTask: Int? = null,
    @SerializedName("scheduled_maintenance_mechanic_task_name")
    val scheduledMaintenanceMechanicTaskName: String? = null,
    @SerializedName("scheduled_maintenance_mechanic_task_desc")
    val scheduledMaintenanceMechanicTaskDesc: String? = null
)