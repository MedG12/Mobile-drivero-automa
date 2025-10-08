package com.automa.domain.breakdown_report.model

data class BreakdownReportPhotoModel(
    val id: Int,
    val idCurativeMaintenanceSubmit: Int,
    val link: String,
    val desc: String,
    val createdOn: String
)