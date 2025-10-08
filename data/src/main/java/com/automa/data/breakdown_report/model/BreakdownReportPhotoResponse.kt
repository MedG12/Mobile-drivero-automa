package com.automa.data.breakdown_report.model

import com.google.gson.annotations.SerializedName

data class BreakdownReportPhotoResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_curative_maintenance_submit")
    val idCurativeMaintenanceSubmit: Int? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("desc")
    val desc: String? = null,
    @SerializedName("created_on")
    val createdOn: String? = null
)