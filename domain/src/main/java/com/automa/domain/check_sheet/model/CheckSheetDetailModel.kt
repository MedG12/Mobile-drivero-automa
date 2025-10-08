package com.automa.domain.check_sheet.model

import com.google.gson.annotations.SerializedName

data class CheckSheetDetailModel(
    val id: Int,
    val desc: String,
    val checked: Int,
    val createdOn: String,
    val modifiedOn: String,
    val idCheckSheetDo: Int,
    val doId: Int,
    val doNumber: String,
    val idCheckSheetDetail: Int,
    val notes: String,
    val checkSheetDetailActivityName: String,
    val idCheckSheet: Int,
    val checkSheetName: String,
    val checkSheetDesc: String
)
