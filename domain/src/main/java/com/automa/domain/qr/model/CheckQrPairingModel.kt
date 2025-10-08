package com.automa.domain.qr.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckQrPairingModel(
    val id: Int,
    val idCompany: Int,
    val company: String,
    val idCarGeneralType: Int,
    val carGeneralType: String,
    val idCarBrands: Int,
    val carBrands: String,
    val idCarType: Int,
    val carType: String,
    val regNumber: String,
    val regNumberWithDoor: String,
    val regYear: Int,
    val manufactureYear: Int,
    val cylCap: Double,
    val vehicleIdNumber: String,
    val engineNumber: String,
    val active: Int,
    val qrLinkSolar: String
): Parcelable