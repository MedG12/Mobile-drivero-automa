package com.automa.domain.driver_task.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryOrderModel(
    val id: Int,
    val idCompany: Int,
    val companyName: String,
    val companyPhone: String,
    val deliveryOrderNumber: String,
    val deliveryOrderDesc: String,
    val idFleet: Int,
    val fleetPlate: String,
    val restInterval: Int,
    val intervalBetweenRest: Int,
    val idDriver: Int,
    val driverName: String,
    val driverImageLink: String,
    val driverPhone: String,
    val idDriverAssistant: Int,
    val driverAssistantImageLink: String,
    val idSecurity: Int,
    val securityName: String,
    val driverAssistantName: String,
    val assignedDate: String,
    val endAssignedDate: String,
    val closeStatus: Int,
    val reportLink: String
): Parcelable
