package com.automa.domain.driver_task

import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.model.*

interface DriverTaskRepository {
    suspend fun getDeliveryOrderList(day: Int, driverId: Int, driverDone: Int?): ResultWrapper<List<DeliveryOrderModel>>
    suspend fun getDeliveryOrderListById(id: Int): ResultWrapper<List<DeliveryOrderModel>>
    suspend fun getMasterDeliveryOrderList(idCategory: Int, idSubCategory: Int): ResultWrapper<List<MasterDeliveryOrderItemModel>>
    suspend fun getMasterDeliveryOrderCategory(): ResultWrapper<List<MasterDeliveryOrderCategoryModel>>
    suspend fun getMasterDeliveryOrderSubCategory(idCategory: Int): ResultWrapper<List<MasterDeliveryOrderSubCategoryModel>>
    suspend fun getDeliveryOrderListByFleet(day: Int, driverId: Int, fleetId: Int, driverDone: Int?): ResultWrapper<List<DeliveryOrderModel>>
    suspend fun getWorkOrderList(idDeliveryOrder: Int): ResultWrapper<WorkOrderModel>
    suspend fun getTaskDetail(idDeliveryOrder: Int): ResultWrapper<TaskDetailModel>
    suspend fun uploadPod(idDeliveryOrder: Int, idWorkOrder: Int, imageDesc: String, file: String): ResultWrapper<String>
    suspend fun getImageList(idDeliveryOrder: Int, idWorkOrder: Int): ResultWrapper<ImagePodModel>
    suspend fun duplicateDeliveryOrder(idDeliveryOrder: Int, assignedDate: String, estDepartureTime: String, endAssignedDoDate: String, fleetId: Int, driverId: Int): ResultWrapper<Int>
    suspend fun postTaskDone(idDeliveryOrder: Int): ResultWrapper<Int>
}