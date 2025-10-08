package com.automa.domain.driver_task.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkOrderModel(
    val statusDraft: Boolean,
    val overviewPolyline: String,
    val result: List<WorkOrderItemModel>
): Parcelable

@Parcelize
data class WorkOrderItemModel(
    val id: Int,
    val idDelivery: Int,
    val idCompany: Int,
    val companyName: String,
    val companyPhone: String,
    val idSavedPlace: Int,
    val mainCustomer: String,
    val mainCustomerPhone: String,
    val woNumber: String,
    val woSeq: Int,
    val woDesc: String,
    val woFromName: String,
    val woDestinationName: String,
    val woAddress: String,
    val picName: String,
    val picPhone: String,
    val locName: String,
    val lat: Double,
    val lng: Double
): Parcelable
