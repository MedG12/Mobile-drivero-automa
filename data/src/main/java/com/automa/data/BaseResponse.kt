package com.automa.data

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status") val status: String ?= "",
    @SerializedName("statusCode") val statusCode: Int ?= -1,
    @SerializedName("result") val result: T
)
