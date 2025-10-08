package com.automa.domain.mechanic_task.model

data class MechanicTaskProofModel(
    val id: Int,
    val photoLink: String,
    val name: String,
    val desc: String,
    val createdOn: String,
    val modifiedOn: String,
    val idScheduledMaintenanceMechanicTask: Int,
    val scheduledMaintenanceMechanicTaskName: String,
    val scheduledMaintenanceMechanicTaskDesc: String
)
