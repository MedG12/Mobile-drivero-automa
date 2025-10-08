package com.automa.data.live_monitoring.model

import com.google.gson.annotations.SerializedName

data class LiveMonitoringResponse(
    @SerializedName("fuel_tank_capacity")
    val fuelTankCapacity: Int? = null,
    @SerializedName("current_tank_capacity")
    val currentTankCapacity: Int? = null,
    @SerializedName("fuel_level")
    val fuelLevel: Int? = null,
    @SerializedName("last_update_with_speed")
    val lastUpdateWithSpeed: String? = null,
    @SerializedName("reg_number_with_door")
    val regNumberWithDoor: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("reg_number")
    val regNumber: String? = null,
    @SerializedName("obd_code")
    val obdCode: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lng")
    val lng: Double? = null,
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("wrn")
    val wrn: Int? = null,
    @SerializedName("spd")
    val spd: Int? = null,
    @SerializedName("odometer")
    val odometer: Double? = null
)