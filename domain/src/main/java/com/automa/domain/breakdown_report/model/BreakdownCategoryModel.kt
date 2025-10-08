package com.automa.domain.breakdown_report.model

data class BreakdownCategoryModel(
    val id: Int,
    val idCompany: Int,
    val idCatagoricalSub: Int,
    val mainCatagorical: String,
    val subCatagoricalName: String,
    val name: String,
    val desc: String,
    val idFleet: Int,
    val regNumber: String,
    val idUser: Int,
    val userFirstName: String,
    val idCheckSheetDetails: Any,
    val idStatus: Int,
    val createdOn: String
)