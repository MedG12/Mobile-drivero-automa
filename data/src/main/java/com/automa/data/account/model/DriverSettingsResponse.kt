package com.automa.data.account.model


import com.google.gson.annotations.SerializedName

data class DriverSettingsResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_company")
    val idCompany: Int? = null,
    @SerializedName("company")
    val company: String? = null,
    @SerializedName("company_address")
    val companyAddress: String? = null,
    @SerializedName("setting")
    val setting: String? = null,
    @SerializedName("value")
    val value: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("created_on")
    val createdOn: String? = null
)