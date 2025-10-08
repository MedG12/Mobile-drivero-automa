package com.automa.domain.driver_task.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class TaskDetailModel(
    val distance: Int,
    val distanceEst: Int,
    val distanceAll: Int,
    val overviewPolyline: String,
    val fleetStatus: FleetStatusModel,
    val locationList: List<LocationModel>,
    val delayTime: String,
    val loadingTime: Int,
    val checkInResult: String,
    val checkIn: List<CheckinModel>,
    val doDetails: List<DoDetailModel>
): Parcelable

@Parcelize
data class FleetStatusModel(
    val lat: Double,
    val lon: Double
): Parcelable

@Parcelize
data class LocationModel(
    val lat: Double,
    val lng: Double
): Parcelable

@Parcelize
data class CheckinModel(
    val workOrderFrom: WorkOrderFromModel,
    val timeCheckIn: String,
    val meterToCheckIn: String,
    val loadingTime: String,
    val latitude: Double,
    val longitude: Double,
    val timeCheckOut: @RawValue Any,
    val idCheckIn: String,
    val idCheckOut: String,
    val share: String,
    val location: List<LocationModel>
): Parcelable

@Parcelize
data class WorkOrderFromModel(
    val id: Int,
    val deliveryId: Int,
    val companyDelivery: String,
    val woNumber: String,
    val woSeq: Int,
    val woDesc: String,
    val woDestinationName: String,
    val woAddress: String,
    val picName: String,
    val picPhone: String,
    val locName: String,
    val lat: Double,
    val lng: Double,
    val deptLat: Double,
    val deptLng: Double,
    val distance: Double,
    val realDistance: String,
    val estDepartureTime: String,
    val estArrivalTime: String,
    val deptTime: String,
    val arrivalTime: String,
    val cargoWeight: Double,
    val spName: String,
    val spLat: Double,
    val spLng: Double,
    val spPhone: String,
    val spAddress: String,
    val mcName: String,
    val mcLat: Double,
    val mcLng: Double,
    val mcAddress: String,
    val mcPhone: String,
    val breakTime: Int,
    val createdOn: String
): Parcelable

@Parcelize
data class DoDetailModel(
    val id: Int,
    val idCompany: Int,
    val doNumber: String,
    val doDesc: String,
    val idFleet: Int,
    val fleetPlate: String,
    val idDriver: Int,
    val driverName: String,
    val driverPhotoLink: String,
    val idDriverAssistant: Int,
    val driverAssistantName: String,
    val driverAssistPhotoLink: String,
    val assignedDoDate: String,
    val avoidToll: Int,
    val endAssignedDoDate: String,
    val overviewPolyline: String,
    val draftValue: Int,
    val closeStatus: Int,
    val totalEstDeliveryCost: Double,
    val totalEstCarbon: Double,
    val totalTkm: Double,
    val optimize: Int,
    val created_on: String
): Parcelable