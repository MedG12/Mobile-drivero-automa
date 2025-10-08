package com.automa.data.driver_task.model


import com.google.gson.annotations.SerializedName

data class ImagePodResponse(
    @SerializedName("result") val result: List<ImagePodItemResponse> ?= null
)

data class ImagePodItemResponse(
    @SerializedName("id") val id: Int ?= null,
    @SerializedName("id_work_order") val idWorkOrder: Int ?= null,
    @SerializedName("link") val link: String ?= null,
    @SerializedName("desc") val desc: String ?= null,
    @SerializedName("created_on") val createdOn: String ?= null
)