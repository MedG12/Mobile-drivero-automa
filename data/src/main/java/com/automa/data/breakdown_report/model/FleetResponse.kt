package com.automa.data.breakdown_report.model


import com.google.gson.annotations.SerializedName

data class FleetResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_company")
    val idCompany: Int? = null,
    @SerializedName("company")
    val company: String? = null,
    @SerializedName("id_car_general_type")
    val idCarGeneralType: Int? = null,
    @SerializedName("car_general_type")
    val carGeneralType: String? = null,
    @SerializedName("id_car_brands")
    val idCarBrands: Int? = null,
    @SerializedName("car_brands")
    val carBrands: String? = null,
    @SerializedName("id_car_type")
    val idCarType: Int? = null,
    @SerializedName("car_type")
    val carType: String? = null,
    @SerializedName("reg_number")
    val regNumber: String? = null,
    @SerializedName("door_number")
    val doorNumber: Any? = null,
    @SerializedName("reg_number_with_door")
    val regNumberWithDoor: String? = null,
    @SerializedName("reg_year")
    val regYear: Int? = null,
    @SerializedName("manufacture_year")
    val manufactureYear: Int? = null,
    @SerializedName("cyl_cap")
    val cylCap: Int? = null,
    @SerializedName("vehicle_id_number")
    val vehicleIdNumber: String? = null,
    @SerializedName("engine_number")
    val engineNumber: String? = null,
    @SerializedName("id_fuel_type")
    val idFuelType: Int? = null,
    @SerializedName("fuel_type")
    val fuelType: String? = null,
    @SerializedName("id_obd")
    val idObd: Int? = null,
    @SerializedName("obd_code")
    val obdCode: String? = null,
    @SerializedName("created_on")
    val createdOn: String? = null,
    @SerializedName("fuel_consumption")
    val fuelConsumption: Double? = null,
    @SerializedName("odometer")
    val odometer: Int? = null,
    @SerializedName("exp_kir")
    val expKir: String? = null,
    @SerializedName("exp_pajak")
    val expPajak: String? = null,
    @SerializedName("max_tonnage")
    val maxTonnage: Int? = null,
    @SerializedName("mt_speed_reduction")
    val mtSpeedReduction: Double? = null,
    @SerializedName("rest_interval")
    val restInterval: Int? = null,
    @SerializedName("interval_between_rest")
    val intervalBetweenRest: Int? = null,
    @SerializedName("full_tank_voltage")
    val fullTankVoltage: Double? = null,
    @SerializedName("empty_tank_voltage")
    val emptyTankVoltage: Double? = null,
    @SerializedName("fuel_tank_capacity")
    val fuelTankCapacity: Int? = null,
    @SerializedName("fuel_alert_sensitivity")
    val fuelAlertSensitivity: Int? = null,
    @SerializedName("tkm_cost")
    val tkmCost: Int? = null,
    @SerializedName("empty_weight")
    val emptyWeight: Int? = null,
    @SerializedName("active")
    val active: Int? = null
)