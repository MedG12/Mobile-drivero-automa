package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.DeliveryOrderResponse
import com.automa.domain.driver_task.model.DeliveryOrderModel

class DeliveryOrderMapper: Mapper<List<DeliveryOrderResponse>, List<DeliveryOrderModel>> {
    override fun mapFromResponse(response: List<DeliveryOrderResponse>): List<DeliveryOrderModel> {
        return response.map {
            DeliveryOrderModel(
                id = it.id.orDefault(),
                idCompany = it.idCompany.orDefault(),
                companyName = it.companyName.orDefault(),
                companyPhone = it.companyPhone.orDefault(),
                deliveryOrderNumber = it.deliveryOrderNumber.orDefault(),
                deliveryOrderDesc = it.deliveryOrderDesc.orDefault(),
                idFleet = it.idFleet.orDefault(),
                fleetPlate = it.fleetPlate.orDefault(),
                restInterval = it.restInterval.orDefault(),
                intervalBetweenRest = it.intervalBetweenRest.orDefault(),
                idDriver = it.idDriver.orDefault(),
                driverName = it.driverName.orDefault(),
                driverImageLink = it.driverImageLink.orDefault(),
                driverPhone = it.driverPhone.orDefault(),
                idDriverAssistant = it.idDriverAssistant.orDefault(),
                driverAssistantImageLink = it.driverAssistantImageLink.orDefault(),
                idSecurity = it.idSecurity.orDefault(),
                securityName = it.securityName.orDefault(),
                driverAssistantName = it.driverAssistantName.orDefault(),
                assignedDate = it.assignedDate.orDefault(),
                endAssignedDate = it.endAssignedDate.orDefault(),
                closeStatus = it.closeStatus.orDefault(),
                reportLink = it.reportLink.orDefault()
            )
        }
    }
}