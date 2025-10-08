package com.automa.data.driver_task.remote

import com.automa.data.common.IResponse
import com.automa.data.driver_task.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DriverTaskApiClient {
    @POST("delivery/list")
    suspend fun getDeliveryOrderList(
        @Body body: HashMap<String, Any?>
    ): IResponse<List<DeliveryOrderResponse>>

    @POST("delivery/order/category/list")
    @FormUrlEncoded
    suspend fun getMasterDeliveryOrderList(
        @Field("id_delivery_order_category") idCategory: Int,
        @Field("id_delivery_order_subcategory") idSubCategory: Int
    ): IResponse<List<MasterDeliveryOrderResponse>>

    @POST("delivery/category/list")
    suspend fun getMasterDeliveryOrderCategory(): IResponse<List<MasterDeliveryOrderCategoryResponse>>

    @POST("delivery/subcategory/list")
    @FormUrlEncoded
    suspend fun getMasterDeliveryOrderSubCategory(
        @Field("id_delivery_order_category") idCategory: Int,
    ): IResponse<List<MasterDeliveryOrderSubCategoryResponse>>

    @POST("delivery/list")
    suspend fun getDeliveryOrderListByFleet(
        @Body body: HashMap<String, Any?>
    ): IResponse<List<DeliveryOrderResponse>>

    @POST("delivery/work/list/v2")
    @FormUrlEncoded
    suspend fun getWorkOrderList(@Field("id_delivery_order") idDeliveryOrder: Int): IResponse<WorkOrderResponse>

    @POST("transporter/report/audit")
    @FormUrlEncoded
    suspend fun getTaskDetail(@Field("id") idDelivery: Int, @Field("path_fleet") pathFleet: Int = 1): IResponse<TaskDetailResponse>

    @POST("delivery/work/input/v2/image")
    @Multipart
    suspend fun uploadPod(
        @Part file: MultipartBody.Part,
        @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>
    ): IResponse<UploadPodResponse>

    @POST("delivery/work/list/v2/image")
    @FormUrlEncoded
    suspend fun getImageList(
        @Field("id_delivery_order") idDeliveryOrder: Int,
        @Field("id_work_order") idWorkOrder: Int
    ): IResponse<ImagePodResponse>

    @POST("delivery/work/copy/input")
    @FormUrlEncoded
    suspend fun duplicateDeliveryOrder(
        @Field("id_delivery_order") idDO: Int,
        @Field("assigned_do_date") assignedDoDate: String,
        @Field("est_departure_time") estDepartureTime: String,
        @Field("end_assigned_do_date") endAssignedDoDate: String,
        @Field("id_fleet") idFleet: Int,
        @Field("id_driver") idDriver: Int
    ): IResponse<Int>

    @POST("delivery/driver/done")
    @FormUrlEncoded
    suspend fun postTaskDone(
        @Field("id") idDo: Int
    ): IResponse<Int>
}