package com.automa.domain.account.model

data class DriverSettingsModel(
    val id: Int,
    val idCompany: Int,
    val company: String,
    val companyAddress: String,
    val setting: String,
    val value: String,
    val status: Int,
    val createdOn: String
)