package com.automa.data.breakdown_report.model

import com.google.gson.annotations.SerializedName

data class BreakdownSubCategoryResponse(
    @SerializedName("id") val id: Int ?= null,
    @SerializedName("id_categorical") val idCategory: Int ?= null,
    @SerializedName("name") val name: String ?= null,
    @SerializedName("desc") val desc: String ?= null
)
