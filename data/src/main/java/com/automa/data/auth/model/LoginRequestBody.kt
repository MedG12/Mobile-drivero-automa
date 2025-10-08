package com.automa.data.auth.model

import com.google.gson.annotations.SerializedName

data class LoginRequestBody(
    @SerializedName("email") val username: String,
    @SerializedName("password") val password: String
)
