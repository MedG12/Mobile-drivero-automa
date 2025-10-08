package com.automa.domain.breakdown_report.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class BreakdownReportModel(
    val `data`: List<BreakdownReportItemModel>
)

@Parcelize
data class BreakdownReportItemModel(
    val id: Int,
    val idCompany: Int,
    val idCategoricalSub: Int,
    val mainCategorical: String,
    val subCategoricalName: String,
    val name: String,
    val desc: String,
    val idFleet: Int,
    val regNumber: String,
    val idUser: Int,
    val userFirstName: String,
    val idCheckSheetDetails: @RawValue Any,
    val idStatus: Int,
    val createdOn: String,
    val level: String,
    val isStoring: Boolean,
    val storingReason: String
): Parcelable