package com.automa.domain.auth.usecase

import com.automa.domain.auth.AuthRepository
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class RegisterMechanicUseCase @Inject constructor(
    private val authRepository: AuthRepository
): BaseUseCase<HashMap<String, Any>>() {
    private var nik = ""
    private var idMechanicPosition = 2
    private var firstName = ""
    private var lastName = ""
    private var birthDate = ""
    private var joinDate = ""
    private var address = ""
    private var isActive = true
    private var whatsappNumber = ""
    private var phoneNumber = ""
    private var email = ""
    private var username = ""
    private var password = ""

    fun addParams(
        nik: String,
        firstName: String,
        lastName: String,
        birthDate: String,
        joinDate: String,
        address: String,
        whatsappNumber: String,
        phoneNumber: String,
        email: String,
        username: String,
        password: String
    ) = apply {
        this.nik = nik
        this.firstName = firstName
        this.lastName = lastName
        this.birthDate = birthDate
        this.joinDate = joinDate
        this.address = address
        this.whatsappNumber = whatsappNumber
        this.phoneNumber = phoneNumber
        this.email = email
        this.username = username
        this.password = password
    }

    override suspend fun execute(): ResultWrapper<HashMap<String, Any>> {
        val result = authRepository.registerMechanic(
            nik = nik,
            idMechanicPosition = idMechanicPosition,
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate,
            joinDate = joinDate,
            address = address,
            isActive = isActive.toString(),
            whatsappNumber = whatsappNumber,
            phoneNumber = phoneNumber,
            email = email
        )

        return if (result is ResultWrapper.Success) {
            authRepository.createMechanic(
                idmechanic = (result.data["id"] as Double).toInt(),
                username = username,
                password = password
            )
        } else {
            result
        }
    }
}