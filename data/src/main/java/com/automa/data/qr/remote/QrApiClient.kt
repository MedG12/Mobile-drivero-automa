package com.automa.data.qr.remote

import com.automa.data.common.IResponse
import com.automa.data.qr.model.CheckQrPairingResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface QrApiClient {
    @POST("qr/general/check")
    @FormUrlEncoded
    suspend fun checkQr(
        @Field("token") token: String
    ): IResponse<Boolean>

    @POST("fleet/list")
    @FormUrlEncoded
    suspend fun checkFleet(
        @Field("token") token: String
    ): IResponse<List<CheckQrPairingResponse>>

    @POST("fleet/list")
    @FormUrlEncoded
    suspend fun checkQrMechanicMaintenance(
        @Field("token") token: String,
        @Field("id_scheduled_maintenance") idScheduledMaintenance: Int
    ): IResponse<List<CheckQrPairingResponse>>

    @POST("driver/binding/fleet/list")
    @FormUrlEncoded
    suspend fun fleetPairing(
        @Field("id_driver") driverId: Int,
        @Field("id_fleet") fleetId: Int
    ): IResponse<List<CheckQrPairingResponse>>
}