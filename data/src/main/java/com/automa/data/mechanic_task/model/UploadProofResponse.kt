package com.automa.data.mechanic_task.model

import com.google.gson.annotations.SerializedName

data class UploadProofResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("link") val link: String,
    @SerializedName("desc") val desc: String
)
