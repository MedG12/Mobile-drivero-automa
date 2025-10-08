package com.automa.domain.check_sheet.model

data class CheckSheetModel(
    val id: Int,
    val idCheckSheet: Int,
    val checkSheetName: String,
    val checkSheetDesc: String,
    val idDo: Int,
    val doNumber: String,
    val idCheckSheetApprovalType: Int,
    val checkSheetApprovalTypeName: String,
    val createdOn: String,
    val modifiedOn: String,
    val isApprove: Int
)
