package com.automa.data.auth

import com.automa.data.auth.mapper.LoginMapper
import com.automa.data.auth.model.LoginRequestBody
import com.automa.data.auth.remote.AuthApiClient
import com.automa.data.common.Mapper
import com.automa.data.common.mapToResult
import com.automa.domain.auth.AuthRepository
import com.automa.domain.auth.model.LoginModel
import com.automa.domain.common.ResultWrapper
import kotlin.Exception

class AuthRepositoryImpl(private val api: AuthApiClient): AuthRepository {
//    override suspend fun requestLogin(username: String, password: String): ResultWrapper<LoginModel> {
//        return try {
//            api.requestLogin(LoginRequestBody(username, password)).mapToResult(LoginMapper())
//        } catch (e: Exception) {
//            ResultWrapper.fail(e.localizedMessage ?: "Error")
//        }
//    }

    override suspend fun requestLogin(
        username: String,
        password: String
    ): ResultWrapper<LoginModel> {
        return try {
            api.requestLogin(LoginRequestBody(username, password)).mapToResult(LoginMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun registerMechanic(
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
    ): ResultWrapper<HashMap<String, Any>> {
        return try {
            api.registerMechanic(nik, idMechanicPosition, firstName, lastName,
                birthDate, joinDate, address, isActive, whatsappNumber, phoneNumber, email
            ).mapToResult(object : Mapper<HashMap<String, Any>, HashMap<String, Any>> {
                override fun mapFromResponse(response: HashMap<String, Any>): HashMap<String, Any> {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun createMechanic(
        idmechanic: Int,
        username: String,
        password: String
    ): ResultWrapper<HashMap<String, Any>> {
        return try {
            api.createMechanic(idmechanic, username, password)
                .mapToResult(object : Mapper<HashMap<String, Any>, HashMap<String, Any>> {
                override fun mapFromResponse(response: HashMap<String, Any>): HashMap<String, Any> {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}