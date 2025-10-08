package com.automa.data.check_sheet.model


import com.google.gson.annotations.SerializedName

data class CheckSheetDetailResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("desc") val desc: String? = null,
    @SerializedName("checked") val checked: Int? = null,
    @SerializedName("created_on") val createdOn: String? = null,
    @SerializedName("modified_on") val modifiedOn: String? = null,
    @SerializedName("id_check_sheet_do") val idCheckSheetDo: Int? = null,
    @SerializedName("do_id") val doId: Int? = null,
    @SerializedName("do_number") val doNumber: String? = null,
    @SerializedName("id_check_sheet_detail") val idCheckSheetDetail: Int? = null,
    @SerializedName("notes") val notes: String ?= null,
    @SerializedName("check_sheet_detail_activity_name") val checkSheetDetailActivityName: String? = null,
    @SerializedName("id_check_sheet") val idCheckSheet: Int? = null,
    @SerializedName("check_sheet_name") val checkSheetName: String? = null,
    @SerializedName("check_sheet_desc") val checkSheetDesc: String? = null
)