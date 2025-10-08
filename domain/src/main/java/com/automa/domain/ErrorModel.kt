package com.automa.domain

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("status") val status: String ?= null,
    @SerializedName("statusCode") val statusCode: Int ?= null,
    @SerializedName("message") val message: String ?= null
)
