package com.automa.domain.account.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CertificationMechanicModel(
    val id: Int,
    val name: String,
    val desc: String,
    val certDate: String,
    val certExpirationDate: String,
    val level: String,
    val idMechanic: Int,
    val mechanicNik: String,
    val mechanicFirstName: String,
    val mechanicLastName: String,
    val mechanicAddress: String,
    val mechanicPhotoLink: String,
    val mechanicWhatsappNumber: String,
    val idMechanicPosition: Int,
    val mechanicPositionName: String
): Parcelable