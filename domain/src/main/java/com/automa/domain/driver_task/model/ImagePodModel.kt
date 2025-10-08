package com.automa.domain.driver_task.model

import com.google.gson.annotations.SerializedName

data class ImagePodModel(
    val result: List<ImagePodItemModel>
)

data class ImagePodItemModel(
    val id: Int,
    val idWorkOrder: Int,
    val link: String,
    val desc: String,
    val createdOn: String
)
