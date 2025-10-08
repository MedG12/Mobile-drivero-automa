package com.automa.data.qr.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.qr.model.CheckQrPairingResponse
import com.automa.domain.qr.model.CheckQrPairingModel

class CheckQrPairingMapper: Mapper<List<CheckQrPairingResponse>, List<CheckQrPairingModel>> {
    override fun mapFromResponse(response: List<CheckQrPairingResponse>): List<CheckQrPairingModel> {
        return response.map {
            CheckQrPairingModel(
                id = it.id.orDefault(),
                idCompany = it.idCompany.orDefault(),
                company = it.company.orDefault(),
                idCarGeneralType = it.idCarGeneralType.orDefault(),
                carGeneralType = it.carGeneralType.orDefault(),
                idCarBrands = it.idCarBrands.orDefault(),
                carBrands = it.carBrands.orDefault(),
                idCarType = it.idCarType.orDefault(),
                carType = it.carType.orDefault(),
                regNumber = it.regNumber.orDefault(),
                regNumberWithDoor = it.regNumberWithDoor.orDefault(),
                regYear = it.regYear.orDefault(),
                manufactureYear = it.manufactureYear.orDefault(),
                cylCap = it.cylCap.orDefault(),
                vehicleIdNumber = it.vehicleIdNumber.orDefault(),
                engineNumber = it.engineNumber.orDefault(),
                active = it.active.orDefault(),
                qrLinkSolar = it.qrLinkSolar.orEmpty()
            )
        }
    }
}