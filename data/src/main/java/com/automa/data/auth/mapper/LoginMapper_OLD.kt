package com.automa.data.auth.mapper

import com.automa.data.auth.model.LoginResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.auth.model.LoginModel

//class LoginMapper_OLD: Mapper<LoginResponse, LoginModel> {
//    override fun mapFromResponse(response: LoginResponse): LoginModel {
//        return LoginModel(
//            data = LoginData(
//                idDriverAppUser = response.data.idDriverAppUser.orDefault(),
//                roleId = response.data.roleId.orDefault(),
//                roles = response.data.roles.orDefault(),
//                id = response.data.id.orDefault(),
//                idCompany = response.data.id.orDefault(),
//                idDriver = response.data.idDriver.orDefault(),
//                username = response.data.username.orDefault(),
//                email = response.data.email.orDefault(),
//                createdOn = response.data.createdOn.orDefault(),
//                driverName = response.data.driverName.orDefault(),
//                driverPhone = response.data.driverPhone.orDefault(),
//                driverKtp = response.data.driverKtp.orDefault(),
//                driverDob = response.data.driverDob.orDefault(),
//                idDriverLicense = response.data.idDriverLicense.orDefault(),
//                licenseType = response.data.licenseType.orDefault(),
//                licenseNumber = response.data.licenseNumber.orDefault(),
//                licenceExpirationDate = response.data.licenceExpirationDate.orDefault(),
//                driverCreatedOn = response.data.driverCreatedOn.orDefault(),
//                imageLink = response.data.imageLink.orDefault(),
//                idImageLog = response.data.idImageLog.orDefault(),
//                imageDesc = response.data.imageDesc.orDefault(),
//                idLicenseImageLog = response.data.idLicenseImageLog.orDefault(),
//                licenseImageLink = response.data.licenseImageLink.orDefault(),
//                licenseImageDesc = response.data.licenseImageDesc.orDefault()
//            ),
//            token = response.token
//        )
//    }
//}