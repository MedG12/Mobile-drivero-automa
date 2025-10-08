package com.automa.data.account.remote

import com.automa.data.account.model.CertificationMechanicResponse
import com.automa.data.account.model.ProfileDriverResponse
import com.automa.data.account.model.ProfileMechanicResponse
import com.automa.data.account.model.DriverSettingsResponse
import com.automa.data.common.IResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountApiClient {
    @POST("mechanic/list")
    @FormUrlEncoded
    suspend fun getMechanicProfile(
        @Field("user_id") id: Int
    ): IResponse<List<ProfileMechanicResponse>>

    @POST("driver/list/driver")
    @FormUrlEncoded
    suspend fun getDriverProfile(
        @Field("user_id") id: Int
    ): IResponse<List<ProfileDriverResponse>>

    @POST("mechanic/certification/list")
    @FormUrlEncoded
    suspend fun getMechanicCertification(
        @Field("id_mechanic") idMechanic: Int
    ): IResponse<List<CertificationMechanicResponse>>

    @POST("mechanic/certification/input")
    @FormUrlEncoded
    suspend fun inputMechanicCertification(
        @Field("id_mechanic") idMechanic: Int,
        @Field("name") name: String,
        @Field("desc") desc: String,
        @Field("cert_date") certDate: String,
        @Field("cert_expiration_date") expDate: String,
        @Field("level") level: String
    ): IResponse<HashMap<String, Any>>

    @POST("mechanic/certification/edit")
    @FormUrlEncoded
    suspend fun updateMechanicCertification(
        @Field("id") id: Int,
        @Field("id_mechanic") idMechanic: Int,
        @Field("name") name: String,
        @Field("desc") desc: String,
        @Field("cert_date") certDate: String,
        @Field("cert_expiration_date") expDate: String,
        @Field("level") level: String
    ): IResponse<String>

    @POST("mechanic/certification/delete")
    @FormUrlEncoded
    suspend fun deleteMechanicCertification(
        @Field("id") id: Int
    ): IResponse<String>

    @POST("user/settinglist")
    @FormUrlEncoded
    suspend fun getDriverSettings(
        @Field("value") filter: Int = 10
    ): IResponse<List<DriverSettingsResponse>>
}