package com.automa.domain.account.model

data class ProfileMechanicModel(
    val id: Int,
    val nik: String,
    val idCompany: Int,
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val joinDate: String,
    val leaveDate: String,
    val address: String,
    val isActive: Int,
    val unactiveReason: String,
    val photoLink: String,
    val whatsappNumber: String,
    val phoneNumber: String,
    val email: String,
    val experienceRemark: String,
    val idMechanicPosition: Int,
    val mechanicPositionName: String
)