package com.automa.data.auth.mapper

import com.automa.data.auth.model.LoginResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.auth.model.LoginModel
import com.automa.domain.auth.model.LoginSubsDataModel

class LoginMapper: Mapper<LoginResponse, LoginModel> {
    override fun mapFromResponse(response: LoginResponse): LoginModel {
        return LoginModel(
            token = response.token,
            userId = response.userId.orDefault(),
            name = response.name.orDefault(),
            email = response.email.orDefault(),
            emailVerified = response.emailVerified.orDefault(),
            idCompany = response.idCompany.orDefault(),
            companyName = response.companyName.orDefault(),
            companyPhone = response.companyPhone.orDefault(),
            idRoles = response.idRoles.orDefault(),
            roleName = response.roleName.orDefault(),
            subsData = LoginSubsDataModel(
                idPlan = response.subsData.idPLan.orDefault(),
                planName = response.subsData.planName.orDefault(),
                totalFleet = response.subsData.totalFleet.orDefault(),
                userLevel = response.subsData.userLevel.orDefault(),
                userAmount = response.subsData.userAmount.orDefault(),
                managerApproval = response.subsData.managerApproval.orDefault(),
                securityApproval = response.subsData.securityApproval.orDefault(),
                totalDriver = response.subsData.totalDriver.orDefault(),
                totalDriverAss = response.subsData.totalDriverAss.orDefault(),
                customer = response.subsData.customer.orDefault(),
                fleetHistory = response.subsData.fleetHistory.orDefault(),
                advanceSchedule = response.subsData.advanceSchedule.orDefault(),
                ddor = response.subsData.ddor.orDefault(),
                utilityReport = response.subsData.utilityReport.orDefault(),
                disconnectedReport = response.subsData.disconnectedReport.orDefault(),
                sharedTrack = response.subsData.sharedTrack.orDefault()
            )
        )
    }
}