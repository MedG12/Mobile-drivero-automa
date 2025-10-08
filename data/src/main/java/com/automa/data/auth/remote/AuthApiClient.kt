package com.automa.data.auth.remote

import com.automa.data.auth.model.LoginRequestBody
import com.automa.data.auth.model.LoginResponse
import com.automa.data.common.IResponse
import retrofit2.http.*

interface AuthApiClient {
    companion object {
        private const val TEMP_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMjgsImV" +
                "tYWlsIjoiYWNjb3VudF9kZW1vQGF1dG9tYS5jb20iLCJuYW1lIjoiRGVtbyIsImNvbXBhbnkiOjI2LCJjb2" +
                "1wYW55X25hbWUiOiJBY2NvdW50IERlbW8iLCJjb21wYW55X3Bob25lIjoiNjI4OTY3MjA1NTk1MSIsInJvb" +
                "GVzX2lkIjo0LCJyb2xlcyI6IlN1cGVyIEFkbWluIiwiaWF0IjoxNjY4NDgyNjcyLCJleHAiOjE5ODM4NDI2" +
                "NzJ9.fnujGSMMFC0DGXcCQTuy9saKPKV4u0WRcMFA5yFExys"
    }

//    @Deprecated("Unused")
//    @POST("driver/app-user/login")
//    suspend fun requestLogin(@Body body: LoginRequestBody): IResponse<LoginResponse>

    @POST("user/login")
    suspend fun requestLogin(@Body body: LoginRequestBody): IResponse<LoginResponse>

    @POST("mechanic/input")
    @FormUrlEncoded
    suspend fun registerMechanic(
        @Field("nik") nik: String,
        @Field("id_mechanic_position") idMechanicPosition: Int,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("birth_date") birthDate: String,
        @Field("join_date") joinDate: String,
        @Field("address") address: String,
        @Field("is_active") isActive: String,
        @Field("whatsapp_number") whatsappNumber: String,
        @Field("phone_number") phoneNumber: String,
        @Field("email") email: String
    ): IResponse<HashMap<String, Any>>

    @POST("mechanic/app-user/input")
    @FormUrlEncoded
    suspend fun createMechanic(
        @Field("id_mechanic") idMechanic: Int,
        @Field("username") username: String,
        @Field("password") password: String
    ): IResponse<HashMap<String, Any>>
}