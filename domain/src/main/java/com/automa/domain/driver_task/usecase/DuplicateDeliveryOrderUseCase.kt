package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import javax.inject.Inject

class DuplicateDeliveryOrderUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<Int>() {
    private var idDO = -1
    private var assignedDate = ""
    private var estDepartureTime = ""
    private var endAssignedDate = ""
    private var idFleet = -1
    private var idDriver = -1

    fun addParams(idDO: Int, assignedDate: String,
                  estDepartureTime: String, endAssignedDate: String,
                  idFleet: Int, idDriver: Int
    ) = apply {
        this.idDO = idDO
        this.assignedDate = assignedDate
        this.estDepartureTime = estDepartureTime
        this.endAssignedDate = endAssignedDate
        this.idFleet = idFleet
        this.idDriver = idDriver
    }

    override suspend fun execute(): ResultWrapper<Int> {
        return repository.duplicateDeliveryOrder(idDO, assignedDate, estDepartureTime, endAssignedDate, idFleet, idDriver)
    }
}