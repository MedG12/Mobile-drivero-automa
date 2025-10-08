package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.TaskDetailResponse
import com.automa.domain.driver_task.model.*

class TaskDetailMapper: Mapper<TaskDetailResponse, TaskDetailModel> {
    override fun mapFromResponse(response: TaskDetailResponse): TaskDetailModel {
        val checkIns = response.checkin
        val locations = response.locationList
        val fleetStat = response.fleetStatus
        val doDetails = response.doDetails

        return TaskDetailModel(
            distance = response.distance.orDefault(),
            distanceEst = response.distanceEst.orDefault(),
            distanceAll = response.distanceAll.orDefault(),
            overviewPolyline = response.overviewPolyline.orDefault(),
            fleetStatus = FleetStatusModel(
                lat = fleetStat?.lat.orDefault(),
                lon = fleetStat?.lon.orDefault()
            ),
            locationList = locations?.filter { it?.lat != null && it.lng != null }?.map {
                LocationModel(
                    lat = it?.lat.orDefault(),
                    lng = it?.lng.orDefault()
                )
            } ?: emptyList(),
            delayTime = response.delayTime.orDefault(),
            loadingTime = response.loadingTime.orDefault(),
            checkInResult = response.checkinresult.orDefault(),
            checkIn = checkIns?.map {
                val wo = it?.workOrderFrom
                CheckinModel(
                    workOrderFrom = WorkOrderFromModel(
                        id = wo?.id.orDefault(),
                        deliveryId = wo?.deliveryId.orDefault(),
                        companyDelivery = wo?.companyDelivery.orDefault(),
                        woNumber = wo?.woNumber.orDefault(),
                        woSeq = wo?.woSeq.orDefault(),
                        woDesc = wo?.woDesc.orDefault(),
                        woDestinationName = wo?.woDestinationName.orDefault(),
                        woAddress = wo?.woAddress.orDefault(),
                        picName = wo?.picName.orDefault(),
                        picPhone = wo?.picPhone.orDefault(),
                        locName = wo?.locName.orDefault(),
                        lat = wo?.lat.orDefault(),
                        lng = wo?.lng.orDefault(),
                        deptLat = wo?.deptLat.orDefault(),
                        deptLng = wo?.deptLng.orDefault(),
                        distance = wo?.distance.orDefault(),
                        realDistance = wo?.realDistance.orDefault(),
                        estDepartureTime = wo?.estDepartureTime.orDefault(),
                        estArrivalTime = wo?.estArrivalTime.orDefault(),
                        deptTime = wo?.deptTime.orDefault(),
                        arrivalTime = wo?.arrivalTime.orDefault(),
                        cargoWeight = wo?.cargoWeight.orDefault(),
                        spName = wo?.spName.orDefault(),
                        spLat = wo?.spLat.orDefault(),
                        spLng = wo?.spLng.orDefault(),
                        spPhone = wo?.spPhone.orDefault(),
                        spAddress = wo?.spAddress.orDefault(),
                        mcName = wo?.mcName.orDefault(),
                        mcLat = wo?.mcLat.orDefault(),
                        mcLng = wo?.mcLng.orDefault(),
                        mcAddress = wo?.mcAddress.orDefault(),
                        mcPhone = wo?.mcPhone.orDefault(),
                        breakTime = wo?.breakTime.orDefault(),
                        createdOn = wo?.createdOn.orDefault()
                    ),
                    timeCheckIn = it?.timeCheckIn.orDefault(),
                    meterToCheckIn = it?.meterToCheckIn.orDefault(),
                    loadingTime = it?.loadingTime.orDefault(),
                    latitude = it?.latitude.orDefault(),
                    longitude = it?.longitude.orDefault(),
                    timeCheckOut = it?.timeCheckOut.orDefault(),
                    idCheckIn = it?.idCheckIn.orDefault(),
                    idCheckOut = it?.idCheckOut.orDefault(),
                    share = it?.share.orDefault(),
                    location = it?.location?.filter { m-> m?.lat != null && m.lng != null }?.map { loc ->
                        LocationModel(
                            lat = loc?.lat.orDefault(),
                            lng = loc?.lng.orDefault()
                        )
                    } ?: emptyList(),
                )
            } ?: emptyList(),
            doDetails = doDetails?.map { p ->
                DoDetailModel(
                    id = p?.id.orDefault(),
                    idCompany = p?.idCompany.orDefault(),
                    doNumber = p?.doNumber.orDefault(),
                    doDesc = p?.doDesc.orDefault(),
                    idFleet = p?.idFleet.orDefault(),
                    fleetPlate = p?.fleetPlate.orDefault(),
                    idDriver = p?.idDriver.orDefault(),
                    driverName = p?.driverName.orDefault(),
                    driverPhotoLink = p?.driverPhotoLink.orDefault(),
                    idDriverAssistant = p?.idDriverAssistant.orDefault(),
                    driverAssistantName = p?.driverAssistantName.orDefault(),
                    driverAssistPhotoLink = p?.driverAssistPhotoLink.orDefault(),
                    assignedDoDate = p?.assignedDoDate.orDefault(),
                    avoidToll = p?.avoidToll.orDefault(),
                    endAssignedDoDate = p?.endAssignedDoDate.orDefault(),
                    overviewPolyline = p?.overviewPolyline.orDefault(),
                    draftValue = p?.draftValue.orDefault(),
                    closeStatus = p?.closeStatus.orDefault(),
                    totalEstDeliveryCost = p?.totalEstDeliveryCost.orDefault(),
                    totalEstCarbon = p?.totalEstCarbon.orDefault(),
                    totalTkm = p?.totalTkm.orDefault(),
                    optimize = p?.optimize.orDefault(),
                    created_on = p?.createdOn.orDefault()
                )
            } ?: emptyList()
        )
    }
}