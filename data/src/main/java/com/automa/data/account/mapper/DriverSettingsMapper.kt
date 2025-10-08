package com.automa.data.account.mapper

import com.automa.data.account.model.DriverSettingsResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.account.model.DriverSettingsModel

class DriverSettingsMapper: Mapper<List<DriverSettingsResponse>, List<DriverSettingsModel>> {
    override fun mapFromResponse(response: List<DriverSettingsResponse>): List<DriverSettingsModel> {
        return response.map {
            DriverSettingsModel(
                id = it.id.orDefault(),
                idCompany = it.idCompany.orDefault(),
                company = it.company.orDefault(),
                companyAddress = it.companyAddress.orDefault(),
                setting = it.setting.orDefault(),
                value = it.value.orDefault(),
                status = it.status.orDefault(),
                createdOn = it.createdOn.orDefault()
            )
        }
    }
}