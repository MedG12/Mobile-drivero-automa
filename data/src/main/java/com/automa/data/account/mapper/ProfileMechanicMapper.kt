package com.automa.data.account.mapper

import com.automa.data.account.model.ProfileMechanicResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.account.model.ProfileMechanicModel

class ProfileMechanicMapper: Mapper<List<ProfileMechanicResponse>, List<ProfileMechanicModel>> {
    override fun mapFromResponse(response: List<ProfileMechanicResponse>): List<ProfileMechanicModel> {
        return response.map { item ->
            ProfileMechanicModel(
                id = item.id.orDefault(),
                nik = item.nik.orDefault(),
                idCompany = item.idCompany.orDefault(),
                userId = item.userId.orDefault(),
                firstName = item.firstName.orDefault(),
                lastName = item.lastName.orDefault(),
                birthDate = item.birthDate.orDefault(),
                joinDate = item.joinDate.orDefault(),
                leaveDate = item.leaveDate.orDefault(),
                address = item.address.orDefault(),
                isActive = item.isActive.orDefault(),
                unactiveReason = item.unactiveReason.orDefault(),
                photoLink = item.photoLink.orDefault(),
                whatsappNumber = item.whatsappNumber.orDefault(),
                phoneNumber = item.phoneNumber.orDefault(),
                email = item.email.orDefault(),
                experienceRemark = item.experienceRemark.orDefault(),
                idMechanicPosition = item.idMechanicPosition.orDefault(),
                mechanicPositionName = item.mechanicPositionName.orDefault()
            )
        }
    }
}