package com.automa.data.qr.model


import com.google.gson.annotations.SerializedName

data class CheckQrPairingResponse(
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
    @SerializedName("id_package_maintenance_settings")
    val idPackageMaintenanceSettings: Any? = null,
    @SerializedName("last_value_maintenance")
    val lastValueMaintenance: Any? = null,
    @SerializedName("reference_form")
    val referenceForm: Any? = null,
    @SerializedName("id_package_maintenance")
    val idPackageMaintenance: Any? = null,
    @SerializedName("every")
    val every: Any? = null,
    @SerializedName("package_code")
    val packageCode: Any? = null,
    @SerializedName("package_name")
    val packageName: Any? = null,
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
    val cylCap: Double? = null,
    @SerializedName("vehicle_id_number")
    val vehicleIdNumber: String? = null,
    @SerializedName("engine_number")
    val engineNumber: String? = null,
    @SerializedName("id_fuel_type")
    val idFuelType: Int? = null,
    @SerializedName("fuel_type")
    val fuelType: String? = null,
    @SerializedName("id_obd")
    val idObd: Any? = null,
    @SerializedName("obd_code")
    val obdCode: Any? = null,
    @SerializedName("created_on")
    val createdOn: String? = null,
    @SerializedName("fuel_consumption")
    val fuelConsumption: Double? = null,
    @SerializedName("odometer")
    val odometer: Double? = null,
    @SerializedName("exp_kir")
    val expKir: String? = null,
    @SerializedName("exp_pajak")
    val expPajak: String? = null,
    @SerializedName("max_tonnage")
    val maxTonnage: Double? = null,
    @SerializedName("mt_speed_reduction")
    val mtSpeedReduction: Double? = null,
    @SerializedName("rest_interval")
    val restInterval: Double? = null,
    @SerializedName("interval_between_rest")
    val intervalBetweenRest: Double? = null,
    @SerializedName("full_tank_voltage")
    val fullTankVoltage: Double? = null,
    @SerializedName("empty_tank_voltage")
    val emptyTankVoltage: Double? = null,
    @SerializedName("fuel_tank_capacity")
    val fuelTankCapacity: Double? = null,
    @SerializedName("fuel_alert_sensitivity")
    val fuelAlertSensitivity: Double? = null,
    @SerializedName("tkm_cost")
    val tkmCost: Double? = null,
    @SerializedName("empty_weight")
    val emptyWeight: Double? = null,
    @SerializedName("show_rente")
    val showRente: Double? = null,
    @SerializedName("active")
    val active: Int? = null,
    @SerializedName("qr_link_solar")
    val qrLinkSolar: String? = null
)