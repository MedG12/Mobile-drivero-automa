package com.automa.domain.account.model

import com.google.gson.annotations.SerializedName

data class ProfileDriverModel(
    val id: Int,
    val idCompany: Int,
    val idImageLog: Int,
    val linkImage: String,
    val descImage: String,
    val idImageLogLic: String,
    val linkImageLic: String,
    val descImageLic: String,
    val name: String,
    val telp: String,
    val ktp: String,
    val birthDate: String,
    val idDriverLic: Int,
    val licType: String,
    val licNumber: String,
    val expDate: String,
    val email: String,
    val userId: Int,
    val createdOn: String
)
