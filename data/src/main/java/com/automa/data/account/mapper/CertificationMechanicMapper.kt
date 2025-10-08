package com.automa.data.account.mapper

import com.automa.data.account.model.CertificationMechanicResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.account.model.CertificationMechanicModel

class CertificationMechanicMapper: Mapper<List<CertificationMechanicResponse>, List<CertificationMechanicModel>> {
    override fun mapFromResponse(response: List<CertificationMechanicResponse>): List<CertificationMechanicModel> {
        return response.map { item ->
            CertificationMechanicModel(
                id = item.id.orDefault(),
                name = item.name.orDefault(),
                desc = item.desc.orDefault(),
                certDate = item.certDate.orDefault(),
                certExpirationDate = item.certExpirationDate.orDefault(),
                level = item.level.orDefault(),
                idMechanic = item.idMechanic.orDefault(),
                mechanicNik = item.mechanicNik.orDefault(),
                mechanicFirstName = item.mechanicFirstName.orDefault(),
                mechanicLastName = item.mechanicLastName.orDefault(),
                mechanicAddress = item.mechanicAddress.orDefault(),
                mechanicPhotoLink = item.mechanicPhotoLink.orDefault(),
                mechanicWhatsappNumber = item.mechanicWhatsappNumber.orDefault(),
                idMechanicPosition = item.idMechanicPosition.orDefault(),
                mechanicPositionName = item.mechanicPositionName.orDefault()
            )
        }
    }
}