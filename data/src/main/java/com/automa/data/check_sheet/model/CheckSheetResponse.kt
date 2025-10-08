package com.automa.data.check_sheet.model


import com.google.gson.annotations.SerializedName

data class CheckSheetResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("id_check_sheet") val idCheckSheet: Int? = null,
    @SerializedName("check_sheet_name") val checkSheetName: String? = null,
    @SerializedName("check_sheet_desc") val checkSheetDesc: String? = null,
    @SerializedName("id_do") val idDo: Int? = null,
    @SerializedName("do_number") val doNumber: String? = null,
    @SerializedName("id_check_sheet_approval_type") val idCheckSheetApprovalType: Int? = null,
    @SerializedName("check_sheet_approval_type_name") val checkSheetApprovalTypeName: String? = null,
    @SerializedName("created_on") val createdOn: String? = null,
    @SerializedName("modified_on") val modifiedOn: String? = null,
    @SerializedName("is_approve") val isApprove: Int ?= null
)