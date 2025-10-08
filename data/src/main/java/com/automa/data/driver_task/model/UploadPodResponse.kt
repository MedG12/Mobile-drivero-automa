package com.automa.data.driver_task.model

import com.google.gson.annotations.SerializedName

data class UploadPodResponse(
    @SerializedName("result") val uploadResult: UploadPodResultResponse
)

data class UploadPodResultResponse(
    @SerializedName("link") val link: String ?= null
)
