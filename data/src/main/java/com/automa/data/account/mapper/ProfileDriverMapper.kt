package com.automa.data.account.mapper

import com.automa.data.account.model.ProfileDriverResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.account.model.ProfileDriverModel

class ProfileDriverMapper: Mapper<List<ProfileDriverResponse>, List<ProfileDriverModel>> {
    override fun mapFromResponse(response: List<ProfileDriverResponse>): List<ProfileDriverModel> {
        return response.map {
            ProfileDriverModel(
                id = it.id.orDefault(),
                idCompany = it.idCompany.orDefault(),
                idImageLog = it.idImageLog.orDefault(),
                linkImage = it.linkImage.orDefault(),
                descImage = it.descImage.orDefault(),
                idImageLogLic = it.idImageLogLic.orDefault(),
                linkImageLic = it.linkImageLic.orDefault(),
                descImageLic = it.descImageLic.orDefault(),
                name = it.name.orDefault(),
                telp = it.telp.orDefault(),
                ktp = it.ktp.orDefault(),
                birthDate = it.birthDate.orDefault(),
                idDriverLic = it.idDriverLic.orDefault(),
                licType = it.licType.orDefault(),
                licNumber = it.licNumber.orDefault(),
                expDate = it.expDate.orDefault(),
                email = it.email.orDefault(),
                userId = it.userId.orDefault(),
                createdOn = it.createdOn.orDefault()
            )
        }
    }
}