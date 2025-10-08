package com.automa.data.base

import androidx.datastore.core.DataStore
import com.automa.datastore.user_data.UserDataModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val dataStore: DataStore<UserDataModel>
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header("Authorization", runBlocking { dataStore.data.first().token })
            .build()

        return chain.proceed(request)
    }
}