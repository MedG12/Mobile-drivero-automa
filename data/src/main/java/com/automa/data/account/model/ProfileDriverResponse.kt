package com.automa.data.account.model


import com.google.gson.annotations.SerializedName

data class ProfileDriverResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_company")
    val idCompany: Int? = null,
    @SerializedName("id_image_log")
    val idImageLog: Int? = null,
    @SerializedName("link_image")
    val linkImage: String? = null,
    @SerializedName("desc_image")
    val descImage: String? = null,
    @SerializedName("id_image_log_lic")
    val idImageLogLic: String? = null,
    @SerializedName("link_image_lic")
    val linkImageLic: String? = null,
    @SerializedName("desc_image_lic")
    val descImageLic: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("telp")
    val telp: String? = null,
    @SerializedName("ktp")
    val ktp: String? = null,
    @SerializedName("birth_date")
    val birthDate: String? = null,
    @SerializedName("id_driver_lic")
    val idDriverLic: Int? = null,
    @SerializedName("lic_type")
    val licType: String? = null,
    @SerializedName("lic_number")
    val licNumber: String? = null,
    @SerializedName("exp_date")
    val expDate: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("user_id")
    val userId: Int? = null,
    @SerializedName("created_on")
    val createdOn: String? = null
)