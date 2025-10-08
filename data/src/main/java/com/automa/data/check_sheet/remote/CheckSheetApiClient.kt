package com.automa.data.check_sheet.remote

import com.automa.data.check_sheet.model.CheckSheetDetailResponse
import com.automa.data.check_sheet.model.CheckSheetResponse
import com.automa.data.common.IResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CheckSheetApiClient {
    @POST("check-sheet/do/list")
    @FormUrlEncoded
    suspend fun getCheckSheet(@Field("id_do") idDeliveryOrder: Int): IResponse<List<CheckSheetResponse>>

    @POST("check-sheet/do/detail/list")
    @FormUrlEncoded
    suspend fun getCheckSheetDetail(@Field("id_check_sheet_do") idCheckSheet: Int): IResponse<List<CheckSheetDetailResponse>>

    @POST("check-sheet/do/detail/edit")
    @FormUrlEncoded
    suspend fun postCheckSheetItem(
        @Field("id") id: Int,
        @Field("checked") checked: Int,
        @Field("notes") notes: String
    ): IResponse<String>
}