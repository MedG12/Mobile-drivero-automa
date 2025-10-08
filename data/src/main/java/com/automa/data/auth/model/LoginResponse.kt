package com.automa.data.auth.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user_id") val userId: Int ?= null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("email_verified") val emailVerified: Boolean? = null,
    @SerializedName("id_company") val idCompany: Int? = null,
    @SerializedName("company") val companyName: String? = null,
    @SerializedName("company_phone") val companyPhone: String? = null,
    @SerializedName("id_roles") val idRoles: Int? = null,
    @SerializedName("roles") val roleName: String? = null,
    @SerializedName("subs") val subsData: LoginSubsDataResponse,
    @SerializedName("token") val token: String
)

data class LoginSubsDataResponse(
    @SerializedName("id_plan") val idPLan: Int? = null,
    @SerializedName("plan_name") val planName: String? = null,
    @SerializedName("total_fleet") val totalFleet: Int? = null,
    @SerializedName("user_level") val userLevel: String? = null,
    @SerializedName("user_amount") val userAmount: Int? = null,
    @SerializedName("manager_approval") val managerApproval: Int? = null,
    @SerializedName("security_approval") val securityApproval: Int? = null,
    @SerializedName("total_driver") val totalDriver: Int? = null,
    @SerializedName("total_driver_ass") val totalDriverAss: Int? = null,
    @SerializedName("customer") val customer: Int? = null,
    @SerializedName("fleet_history") val fleetHistory: Int? = null,
    @SerializedName("advance_schedule") val advanceSchedule: Int? = null,
    @SerializedName("ddor") val ddor: Int? = null,
    @SerializedName("utility_report") val utilityReport: Int? = null,
    @SerializedName("disconnected_report") val disconnectedReport: Int? = null,
    @SerializedName("shared_track") val sharedTrack: Int? = null
)