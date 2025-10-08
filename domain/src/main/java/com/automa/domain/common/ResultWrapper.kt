package com.automa.domain.common

import com.automa.domain.ErrorModel

sealed class ResultWrapper<T> {
    class Loading<T> : ResultWrapper<T>()
    class Empty<T> : ResultWrapper<T>()
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Failure<T>(val errorData: ErrorModel): ResultWrapper<T>()

    companion object {
        fun <T> loading(): ResultWrapper<T> = Loading()
        fun <T> success(data: T): ResultWrapper<T> = Success(data)
        fun <T> empty(): ResultWrapper<T> = Empty()
        fun <T> fail(message: String): ResultWrapper<T> = Failure(ErrorModel("Error", 0, message))
        fun <T> fail(errorData: ErrorModel): ResultWrapper<T> = Failure(errorData)

        fun <T> ResultWrapper<T>.handleResult(
            onSuccess: (Success<T>)->Unit, onFailure: (Failure<T>)->Unit,
            onEmpty: (() -> Unit)? =null, onLoading: (() -> Unit)? =null){
            when(this){
                is Success -> onSuccess.invoke(this)
                is Failure -> onFailure.invoke(this)
                is Loading -> onLoading?.invoke()
                is Empty -> onEmpty?.invoke()
            }.exhaustive
        }
    }
}

val <T> T.exhaustive : T get() = this