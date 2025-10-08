package com.automa.data.account.model


import com.google.gson.annotations.SerializedName

data class CertificationMechanicResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("desc")
    val desc: String? = null,
    @SerializedName("cert_date")
    val certDate: String? = null,
    @SerializedName("cert_expiration_date")
    val certExpirationDate: String? = null,
    @SerializedName("level")
    val level: String? = null,
    @SerializedName("id_mechanic")
    val idMechanic: Int? = null,
    @SerializedName("mechanic_nik")
    val mechanicNik: String? = null,
    @SerializedName("mechanic_first_name")
    val mechanicFirstName: String? = null,
    @SerializedName("mechanic_last_name")
    val mechanicLastName: String? = null,
    @SerializedName("mechanic_address")
    val mechanicAddress: String? = null,
    @SerializedName("mechanic_photo_link")
    val mechanicPhotoLink: String? = null,
    @SerializedName("mechanic_whatsapp_number")
    val mechanicWhatsappNumber: String? = null,
    @SerializedName("id_mechanic_position")
    val idMechanicPosition: Int? = null,
    @SerializedName("mechanic_position_name")
    val mechanicPositionName: String? = null
)