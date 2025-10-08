package com.automa.domain.common

abstract class BaseUseCase<T> {
    abstract suspend fun execute(): ResultWrapper<T>
}