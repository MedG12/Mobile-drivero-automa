package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.MasterDeliveryOrderResponse
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.domain.driver_task.model.MasterDeliveryOrderItemModel
import com.automa.domain.driver_task.model.MasterDeliveryOrderSubItemModel

class MasterDeliveryOrderMapper: Mapper<List<MasterDeliveryOrderResponse>, List<MasterDeliveryOrderItemModel>> {
    override fun mapFromResponse(response: List<MasterDeliveryOrderResponse>): List<MasterDeliveryOrderItemModel> {
        return response.map {
            MasterDeliveryOrderItemModel(
                id = it.id.orDefault(),
                idCompany = it.idCompany.orDefault(),
                name = it.name.orDefault(),
                createdAt = it.createdAt.orDefault(),
                subCategory = it.subCategory?.map { sub ->
                    MasterDeliveryOrderSubItemModel(
                        id = sub.id.orDefault(),
                        idDeliveryOrderCategory = sub.idDeliveryOrderCategory.orDefault(),
                        name = sub.name.orDefault(),
                        masterDoList = sub.masterDoList?.map { order ->
                            DeliveryOrderModel(
                                id = order.id.orDefault(),
                                idCompany = order.idCompany.orDefault(),
                                companyName = order.companyName.orDefault(),
                                companyPhone = order.companyPhone.orDefault(),
                                deliveryOrderNumber = order.deliveryOrderNumber.orDefault(),
                                deliveryOrderDesc = order.deliveryOrderDesc.orDefault(),
                                idFleet = order.idFleet.orDefault(),
                                fleetPlate = order.fleetPlate.orDefault(),
                                restInterval = order.restInterval.orDefault(),
                                intervalBetweenRest = order.intervalBetweenRest.orDefault(),
                                idDriver = order.idDriver.orDefault(),
                                driverName = order.driverName.orDefault(),
                                driverImageLink = order.driverImageLink.orDefault(),
                                driverPhone = order.driverPhone.orDefault(),
                                idDriverAssistant = order.idDriverAssistant.orDefault(),
                                driverAssistantImageLink = order.driverAssistantImageLink.orDefault(),
                                idSecurity = order.idSecurity.orDefault(),
                                securityName = order.securityName.orDefault(),
                                driverAssistantName = order.driverAssistantName.orDefault(),
                                assignedDate = order.assignedDate.orDefault(),
                                endAssignedDate = order.endAssignedDate.orDefault(),
                                closeStatus = order.closeStatus.orDefault(),
                                reportLink = order.reportLink.orDefault()
                            )
                        } ?: emptyList()
                    )
                } ?: emptyList()
            )
        }
    }
}