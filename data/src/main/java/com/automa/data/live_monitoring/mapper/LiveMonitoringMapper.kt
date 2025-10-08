package com.automa.data.live_monitoring.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.live_monitoring.model.LiveMonitoringResponse
import com.automa.domain.live_monitoring.model.LiveMonitoringModel

class LiveMonitoringMapper: Mapper<List<LiveMonitoringResponse>, List<LiveMonitoringModel>> {
    override fun mapFromResponse(response: List<LiveMonitoringResponse>): List<LiveMonitoringModel> {
        return response.map {
            LiveMonitoringModel(
               fuelTankCapacity = it.fuelTankCapacity.orDefault(),
               currentTankCapacity = it.currentTankCapacity.orDefault(),
               fuelLevel = it.fuelLevel.orDefault(),
               lastUpdateWithSpeed = it.lastUpdateWithSpeed.orDefault(),
               regNumberWithDoor = it.regNumberWithDoor.orDefault(),
               id = it.id.orDefault(),
                regNumber = it.regNumber.orDefault(),
                obdCode = it.obdCode.orDefault(),
                lat = it.lat.orDefault(),
                lng = it.lng.orDefault(),
                time = it.time.orDefault(),
                wrn = it.wrn.orDefault(),
                spd = it.spd.orDefault(),
                odometer = it.odometer.orDefault()
            )
        }
    }
}