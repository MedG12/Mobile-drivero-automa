package com.automa.data.mechanic_task.remote

import com.automa.data.common.IResponse
import com.automa.data.mechanic_task.model.MechanicSubTaskResponse
import com.automa.data.mechanic_task.model.MechanicTaskProofResponse
import com.automa.data.mechanic_task.model.MechanicTaskResponse
import com.automa.data.mechanic_task.model.UploadProofResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MechanicTaskApiClient {
    @POST("scheduled-maintenance/list")
    suspend fun getMechanicTaskList(
        @Body body: HashMap<String, Any?>
    ): IResponse<List<MechanicTaskResponse>>

    @POST("scheduled-maintenance/list")
    suspend fun getMechanicTaskList(): IResponse<List<MechanicTaskResponse>>

    @POST("scheduled-maintenance/workshop/list")
    suspend fun getWorkshopTaskList(
        @Body body: HashMap<String, Any?>
    ): IResponse<List<MechanicTaskResponse>>

    @POST("scheduled-maintenance/workshop/list")
    suspend fun getWorkshopTaskList(): IResponse<List<MechanicTaskResponse>>

    @POST("scheduled-maintenance/mechanic/task/detail/list")
    @FormUrlEncoded
    suspend fun getSubTaskList(
        @Field("id_scheduled_maintenance_mechanic_task") id: Int
    ): IResponse<List<MechanicSubTaskResponse>>

    @POST("scheduled-maintenance/mechanic/task/detail/input")
    @FormUrlEncoded
    suspend fun createTaskDetail(
        @Field("id_scheduled_maintenance_mechanic_task") id: Int,
        @Field("task_name") taskName: String,
        @Field("desc") description: String,
        @Field("cost") cost: Long,
        @Field("duration") duration: Int
    ): IResponse<HashMap<String, Any>>

    @POST("scheduled-maintenance/mechanic/task/detail/edit")
    @FormUrlEncoded
    suspend fun updateTaskDetail(
        @Field("id") idTaskDetail: Int,
        @Field("id_scheduled_maintenance_mechanic_task") id: Int,
        @Field("task_name") taskName: String,
        @Field("desc") description: String,
        @Field("cost") cost: Long,
        @Field("duration") duration: Int
    ): IResponse<String>

    @POST("iot/logging/upload")
    @Multipart
    suspend fun uploadPod(
        @Part file: MultipartBody.Part,
        @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>
    ): IResponse<UploadProofResponse>

    @POST("scheduled-maintenance/mechanic/task/proof/input")
    @FormUrlEncoded
    suspend fun inputTaskProof(
        @Field("id_scheduled_maintenance_mechanic_task") id: Int,
        @Field("photo_link") link: String,
        @Field("name") name: String,
        @Field("desc") description: String
    ): IResponse<HashMap<String, Any>>

    @POST("scheduled-maintenance/mechanic/task/before-proof/input")
    @FormUrlEncoded
    suspend fun inputTaskProofBefore(
        @Field("id_scheduled_maintenance_mechanic_task") id: Int,
        @Field("photo_link") link: String,
        @Field("name") name: String,
        @Field("desc") description: String
    ): IResponse<HashMap<String, Any>>

    @POST("scheduled-maintenance/mechanic/task/proof/delete")
    @FormUrlEncoded
    suspend fun deleteTaskProof(
        @Field("id") id: Int
    ): IResponse<String>

    @POST("scheduled-maintenance/mechanic/task/proof/list")
    @FormUrlEncoded
    suspend fun getListTaskProof(
        @Field("id_scheduled_maintenance_mechanic_task") id: Int
    ): IResponse<List<MechanicTaskProofResponse>>

    @POST("scheduled-maintenance/mechanic/task/before-proof/list")
    @FormUrlEncoded
    suspend fun getListTaskProofBefore(
        @Field("id_scheduled_maintenance_mechanic_task") id: Int
    ): IResponse<List<MechanicTaskProofResponse>>
}