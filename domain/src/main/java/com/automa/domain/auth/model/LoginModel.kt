package com.automa.domain.auth.model

data class LoginModel(
    val userId: Int,
    val name: String,
    val email: String,
    val emailVerified: Boolean,
    val idCompany: Int,
    val companyName: String,
    val companyPhone: String,
    val idRoles: Int,
    val roleName: String,
    val subsData: LoginSubsDataModel,
    val token: String
)

data class LoginSubsDataModel(
    val idPlan: Int,
    val planName: String,
    val totalFleet: Int,
    val userLevel: String,
    val userAmount: Int,
    val managerApproval: Int,
    val securityApproval: Int,
    val totalDriver: Int,
    val totalDriverAss: Int,
    val customer: Int,
    val fleetHistory: Int,
    val advanceSchedule: Int,
    val ddor: Int,
    val utilityReport: Int,
    val disconnectedReport: Int,
    val sharedTrack: Int
)