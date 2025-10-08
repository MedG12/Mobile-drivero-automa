package com.automa.data.common

import com.automa.data.BaseResponse
import com.automa.domain.ErrorModel
import com.automa.domain.common.ResultWrapper
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

typealias IResponse<T> = Response<BaseResponse<T>>

fun <Resp, Model> IResponse<Resp>.mapToResult(mapper: Mapper<Resp, Model>): ResultWrapper<Model> {
    return when {
        this.isSuccessful -> {
            val body = this.body()
            when {
                body?.result != null -> {
                    val mappedResult = mapper.mapFromResponse(body.result)
                    ResultWrapper.success(mappedResult)
                }
                else -> {
                    ResultWrapper.fail(ErrorModel("Error", body?.statusCode ?: this.code(), this.message()))
                }
            }
        }
        else -> {
            val errorModel = parseErrorBody(this.errorBody(), this.code())
            ResultWrapper.fail(errorModel)
        }
    }
}

private fun parseErrorBody(error: ResponseBody?, code: Int): ErrorModel {
    val message = when (code) {
        400 -> "Bad Request"
        403 -> "Forbidden"
        404 -> "Not Found"
        500 -> "Internal Server Error"
        502 -> "Bad Gateway"
        503 -> "Service Unavailable"
        else -> "Unknown Error"
    }
    return try {
        Gson().fromJson(error!!.charStream(), ErrorModel::class.java)
    } catch (e: Exception) {
        ErrorModel("Error", code, message)
    }
}

fun String?.orDefault(): String = this ?: "-"

fun Double?.orDefault(): Double = this ?: 0.0

fun Int?.orDefault(): Int = this ?: -1

fun Float?.orDefault(): Float = this ?: -1f

fun Long?.orDefault(): Long = this ?: -1L

fun Boolean?.orDefault(): Boolean = this ?: false