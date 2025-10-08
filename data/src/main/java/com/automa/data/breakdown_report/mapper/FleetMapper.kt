package com.automa.data.breakdown_report.mapper

import com.automa.data.breakdown_report.model.FleetResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.breakdown_report.model.FleetModel

class FleetMapper: Mapper<List<FleetResponse>, List<FleetModel>> {
    override fun mapFromResponse(response: List<FleetResponse>): List<FleetModel> {
        return response.map { item ->
            FleetModel(
                id = item.id.orDefault(),
                idCompany = item.idCompany.orDefault(),
                company = item.company.orDefault(),
                idCarGeneralType = item.idCarGeneralType.orDefault(),
                carGeneralType = item.carGeneralType.orDefault(),
                idCarBrands = item.idCarBrands.orDefault(),
                carBrands = item.carBrands.orDefault(),
                idCarType = item.idCarType.orDefault(),
                carType = item.carType.orDefault(),
                regNumber = item.regNumber.orDefault(),
                doorNumber = item.doorNumber ?: "",
                regNumberWithDoor = item.regNumberWithDoor.orDefault(),
                regYear = item.regYear.orDefault(),
                cylCap = item.cylCap.orDefault(),
                vehicleIdNumber = item.vehicleIdNumber.orDefault(),
                engineNumber = item.engineNumber.orDefault(),
                idFuelType = item.idFuelType.orDefault(),
                fuelType = item.fuelType.orDefault(),
                idObd = item.idObd.orDefault(),
                obdCode = item.obdCode.orDefault(),
                createdOn = item.createdOn.orDefault(),
                fuelConsumption = item.fuelConsumption.orDefault(),
                odometer = item.odometer.orDefault(),
                expKir = item.expKir.orDefault(),
                expPajak = item.expPajak.orDefault(),
                maxTonnage = item.maxTonnage.orDefault(),
                mtSpeedReduction = item.mtSpeedReduction.orDefault(),
                restInterval = item.restInterval.orDefault(),
                intervalBetweenRest = item.intervalBetweenRest.orDefault(),
                fullTankVoltage = item.fullTankVoltage.orDefault(),
                emptyTankVoltage = item.emptyTankVoltage.orDefault(),
                fuelTankCapacity = item.fuelTankCapacity.orDefault(),
                fuelAlertSensitivity = item.fuelAlertSensitivity.orDefault(),
                tkmCost = item.tkmCost.orDefault(),
                emptyWeight = item.emptyWeight.orDefault(),
                active = item.active.orDefault(),
                manufactureYear = item.manufactureYear.orDefault()
            )
        }
    }
}