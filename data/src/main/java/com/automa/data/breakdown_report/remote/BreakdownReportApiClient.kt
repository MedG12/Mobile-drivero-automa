package com.automa.data.breakdown_report.remote

import com.automa.data.breakdown_report.model.BreakdownCategoryResponse
import com.automa.data.breakdown_report.model.BreakdownReportPhotoResponse
import com.automa.data.breakdown_report.model.BreakdownReportResponse
import com.automa.data.breakdown_report.model.BreakdownSubCategoryResponse
import com.automa.data.breakdown_report.model.FleetResponse
import com.automa.data.common.IResponse
import com.automa.data.driver_task.model.UploadPodResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface BreakdownReportApiClient {
    companion object {
        private const val TEMP_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJlbWF" +
                "pbCI6ImtyaXN0aWFudG9rZWx2aW5AbGl2ZS5jb20iLCJuYW1lIjoiS2VsdmluIiwiY29tcGFueSI6MSwiY2" +
                "9tcGFueV9uYW1lIjoiQXV0b21hIERldiBDb21wYW55IiwiY29tcGFueV9waG9uZSI6IjYyODk2NzIwNTU5NT" +
                "EiLCJyb2xlc19pZCI6NCwicm9sZXMiOiJTdXBlciBBZG1pbiIsImlhdCI6MTY2OTg3MTYyMywiZXhwIjoxOT" +
                "g1MjMxNjIzfQ.fHsHTeZN0Am8aGDtdBGROc_NP2jq3yiFbJTZkoikfWM"
    }

    @POST("curative-maintenance/list")
    suspend fun getCategoryList(): IResponse<List<BreakdownCategoryResponse>>

    @POST("curative-maintenance/sub/catagory/list")
    suspend fun getSubCategoryList(): IResponse<List<BreakdownSubCategoryResponse>>

    @FormUrlEncoded
    @POST("curative-maintenance/submit")
    suspend fun submitBreakdownReport(
        @Field("id_catagorical_sub") idCategorical: Int,
        @Field("name") name: String,
        @Field("desc") desc: String,
        @Field("id_fleet") idFleet: Int,
        @Field("level") urgency: Int,
        @Field("storing") isStoring: Int,
        @Field("storing_reason") storingReason: String
    ): IResponse<HashMap<String, Any>>

    @Multipart
    @POST("curative-maintenance/submit/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>
    ): IResponse<UploadPodResponse>
    @FormUrlEncoded
    @POST("curative-maintenance/list/image")
    suspend fun getImage(
        @Field("id_curative_maintenance_submit") id: Int
    ): IResponse<List<BreakdownReportPhotoResponse>>

    @POST("curative-maintenance/list")
    suspend fun getBreakdownReports(): IResponse<BreakdownReportResponse>

    @POST("fleet/list")
    suspend fun getFleetList(): IResponse<List<FleetResponse>>
}