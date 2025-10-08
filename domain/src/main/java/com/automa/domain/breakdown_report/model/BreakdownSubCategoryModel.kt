package com.automa.domain.breakdown_report.model

import com.google.gson.annotations.SerializedName

data class BreakdownSubCategoryModel(
    val id: Int,
    val idCategory: Int,
    val name: String,
    val desc: String
)