package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.DeliveryOrderModel
import javax.inject.Inject

class DeliveryOrderUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<List<DeliveryOrderModel>>() {
    private var day = 0
    private var driverId = -1
    private var fleetId = -1
    private var driverDone: Int ?= null

    fun addParams(day: Int, driverId: Int, fleetId: Int = -1, driverDone: Int?=null) = apply {
        this.day = day
        this.driverId = driverId
        this.fleetId = fleetId
        this.driverDone = driverDone
    }

    override suspend fun execute(): ResultWrapper<List<DeliveryOrderModel>> {
        return if (fleetId != -1) {
            repository.getDeliveryOrderListByFleet(day, driverId, fleetId, driverDone)
        } else {
            repository.getDeliveryOrderList(day, driverId, driverDone)
        }
    }
}