package com.automa.domain.auth.usecase

import com.automa.domain.auth.AuthRepository
import com.automa.domain.auth.model.LoginModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
): BaseUseCase<LoginModel>() {
    private var params = mapOf<String, Any>()

    fun addParams(username: String, password: String) = apply {
        params = mapOf(
            USERNAME to username,
            PASSWORD to password
        )
    }

    override suspend fun execute(): ResultWrapper<LoginModel> {
        return authRepository.requestLogin(
            params[USERNAME] as String,
            params[PASSWORD] as String
        )
    }

    companion object {
        private const val USERNAME = "USERNAME"
        private const val PASSWORD = "PASSWORD"
    }
}