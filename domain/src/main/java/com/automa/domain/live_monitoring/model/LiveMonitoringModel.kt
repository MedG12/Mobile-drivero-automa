package com.automa.domain.live_monitoring.model

data class LiveMonitoringModel(
    val fuelTankCapacity: Int,
    val currentTankCapacity: Int,
    val fuelLevel: Int,
    val lastUpdateWithSpeed: String,
    val regNumberWithDoor: String,
    val id: Int,
    val regNumber: String,
    val obdCode: String,
    val lat: Double,
    val lng: Double,
    val time: String,
    val wrn: Int,
    val spd: Int,
    val odometer: Double
)