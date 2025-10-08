package com.automa.domain.auth

import com.automa.domain.auth.model.LoginModel
import com.automa.domain.common.ResultWrapper

interface AuthRepository {
//    suspend fun requestLogin(username: String, password: String): ResultWrapper<LoginModel>

    suspend fun requestLogin(username: String, password: String): ResultWrapper<LoginModel>

    suspend fun registerMechanic(
        nik: String,
        idMechanicPosition: Int,
        firstName: String,
        lastName: String,
        birthDate: String,
        joinDate: String,
        address: String,
        isActive: String,
        whatsappNumber: String,
        phoneNumber: String,
        email: String
    ): ResultWrapper<HashMap<String, Any>>

    suspend fun createMechanic(
        idmechanic: Int,
        username: String,
        password: String
    ): ResultWrapper<HashMap<String, Any>>
}