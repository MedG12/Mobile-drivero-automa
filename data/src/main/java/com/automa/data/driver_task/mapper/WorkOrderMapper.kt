package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.WorkOrderResponse
import com.automa.domain.driver_task.model.WorkOrderItemModel
import com.automa.domain.driver_task.model.WorkOrderModel

class WorkOrderMapper: Mapper<WorkOrderResponse, WorkOrderModel> {
    override fun mapFromResponse(response: WorkOrderResponse): WorkOrderModel {
        return WorkOrderModel(
            statusDraft = response.statusDraft.orDefault(),
            overviewPolyline = response.overviewPolyline.orDefault(),
            result = response.result?.map {
                WorkOrderItemModel(
                    id = it.id.orDefault(),
                    idDelivery = it.idDelivery.orDefault(),
                    idCompany = it.idCompany.orDefault(),
                    companyName = it.companyName.orDefault(),
                    companyPhone = it.companyPhone.orDefault(),
                    idSavedPlace = it.idSavedPlace.orDefault(),
                    mainCustomer = it.mainCustomer.orDefault(),
                    mainCustomerPhone = it.mainCustomerPhone.orDefault(),
                    woNumber = it.woNumber.orDefault(),
                    woSeq = it.woSeq.orDefault(),
                    woDesc = it.woDesc.orDefault(),
                    woFromName = it.woFromName.orDefault(),
                    woDestinationName = it.woDestinationName.orDefault(),
                    woAddress = it.woAddress.orDefault(),
                    picName = it.picName.orDefault(),
                    picPhone = it.picPhone.orDefault(),
                    locName = it.locName.orDefault(),
                    lat = it.lat.orDefault(),
                    lng = it.lng.orDefault()
                )
            } ?: emptyList()
        )
    }
}