package com.automa.data.account.model


import com.google.gson.annotations.SerializedName

data class ProfileMechanicResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nik") val nik: String? = null,
    @SerializedName("id_company") val idCompany: Int? = null,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("birth_date") val birthDate: String? = null,
    @SerializedName("join_date") val joinDate: String? = null,
    @SerializedName("leave_date") val leaveDate: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("is_active") val isActive: Int? = null,
    @SerializedName("unactive_reason") val unactiveReason: String? = null,
    @SerializedName("photo_link") val photoLink: String? = null,
    @SerializedName("whatsapp_number") val whatsappNumber: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("experience_remark") val experienceRemark: String? = null,
    @SerializedName("id_mechanic_position") val idMechanicPosition: Int? = null,
    @SerializedName("mechanic_position_name") val mechanicPositionName: String? = null
)