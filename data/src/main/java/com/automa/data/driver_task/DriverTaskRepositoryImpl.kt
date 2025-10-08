package com.automa.data.driver_task

import com.automa.data.common.Mapper
import com.automa.data.common.mapToResult
import com.automa.data.driver_task.mapper.*
import com.automa.data.driver_task.remote.DriverTaskApiClient
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DriverTaskRepositoryImpl(private val apiClient: DriverTaskApiClient): DriverTaskRepository {
    override suspend fun getDeliveryOrderList(day: Int, driverId: Int, driverDone: Int?): ResultWrapper<List<DeliveryOrderModel>> {
        return try {
            val body = hashMapOf<String, Any?>()
            body["day"] = day
            body["id_driver"] = driverId
            body["driver_done"] = driverDone
            apiClient.getDeliveryOrderList(body).mapToResult(DeliveryOrderMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getDeliveryOrderListById(id: Int): ResultWrapper<List<DeliveryOrderModel>> {
        return try {
            val body = hashMapOf<String, Any?>()
            body["id"] = id
            apiClient.getDeliveryOrderList(body).mapToResult(DeliveryOrderMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMasterDeliveryOrderList(
        idCategory: Int,
        idSubCategory: Int
    ): ResultWrapper<List<MasterDeliveryOrderItemModel>> {
        return try {
            apiClient.getMasterDeliveryOrderList(idCategory, idSubCategory).mapToResult(MasterDeliveryOrderMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMasterDeliveryOrderCategory(): ResultWrapper<List<MasterDeliveryOrderCategoryModel>> {
        return try {
            apiClient.getMasterDeliveryOrderCategory().mapToResult(MasterDeliveryOrderCategoryMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMasterDeliveryOrderSubCategory(idCategory: Int): ResultWrapper<List<MasterDeliveryOrderSubCategoryModel>> {
        return try {
            apiClient.getMasterDeliveryOrderSubCategory(idCategory).mapToResult(MasterDeliveryOrderSubCategoryMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getDeliveryOrderListByFleet(day: Int, driverId: Int, fleetId: Int, driverDone: Int?): ResultWrapper<List<DeliveryOrderModel>> {
        return try {
            val body = hashMapOf<String, Any?>()
            body["day"] = day
            body["id_driver"] = driverId
            body["id_fleet"] = fleetId
            body["driver_done"] = driverDone
            apiClient.getDeliveryOrderListByFleet(body).mapToResult(DeliveryOrderMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getWorkOrderList(idDeliveryOrder: Int): ResultWrapper<WorkOrderModel> {
        return try {
            apiClient.getWorkOrderList(idDeliveryOrder).mapToResult(WorkOrderMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getTaskDetail(idDeliveryOrder: Int): ResultWrapper<TaskDetailModel> {
        return try {
            apiClient.getTaskDetail(idDeliveryOrder).mapToResult(TaskDetailMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun uploadPod(
        idDeliveryOrder: Int,
        idWorkOrder: Int,
        imageDesc: String,
        file: String
    ): ResultWrapper<String> {
        return try {
            val imgFile = File(file)
            val requestFile = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val partImage = MultipartBody.Part.createFormData("file", imgFile.name, requestFile)
            val map = mapOf(
                "id_delivery_order" to idDeliveryOrder.toString().toRequestBody("multipart/form-data".toMediaType()),
                "id_work_order" to idWorkOrder.toString().toRequestBody("multipart/form-data".toMediaType()),
                "image_desc" to imageDesc.toRequestBody("multipart/form-data".toMediaType()),
            )
            apiClient.uploadPod(partImage, map).mapToResult(UploadPodMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getImageList(idDeliveryOrder: Int, idWorkOrder: Int): ResultWrapper<ImagePodModel> {
        return try {
            apiClient.getImageList(idDeliveryOrder, idWorkOrder).mapToResult(ImagePodMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun duplicateDeliveryOrder(
        idDeliveryOrder: Int,
        assignedDate: String,
        estDepartureTime: String,
        endAssignedDoDate: String,
        fleetId: Int,
        driverId: Int
    ): ResultWrapper<Int> {
        return try {
            apiClient.duplicateDeliveryOrder(
                idDeliveryOrder,
                assignedDate,
                estDepartureTime,
                endAssignedDoDate,
                fleetId,
                driverId
            ).mapToResult(object : Mapper<Int, Int> {
                override fun mapFromResponse(response: Int): Int {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun postTaskDone(idDeliveryOrder: Int): ResultWrapper<Int> {
        return try {
            apiClient.postTaskDone(idDeliveryOrder).mapToResult(object : Mapper<Int, Int> {
                override fun mapFromResponse(response: Int): Int {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}