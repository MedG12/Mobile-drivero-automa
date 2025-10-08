package com.automa.domain.mechanic_task.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MechanicSubTaskModel(
    val id: Int,
    val cost: Int,
    val taskName: String,
    val desc: String,
    val createdOn: String,
    val modifiedOn: String,
    val idScheduledMaintenanceMechanicTask: Int,
    val scheduledMaintenanceMechanicTaskName: String,
    val scheduledMaintenanceMechanicTaskDesc: String,
    val approval: String,
    val duration: String
): Parcelable
