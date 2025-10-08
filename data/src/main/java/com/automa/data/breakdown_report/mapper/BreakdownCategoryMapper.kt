package com.automa.data.breakdown_report.mapper

import com.automa.data.breakdown_report.model.BreakdownCategoryResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.breakdown_report.model.BreakdownCategoryModel

class BreakdownCategoryMapper: Mapper<List<BreakdownCategoryResponse>, List<BreakdownCategoryModel>> {
    override fun mapFromResponse(response: List<BreakdownCategoryResponse>): List<BreakdownCategoryModel> {
        return response.map { item ->
            BreakdownCategoryModel(
                id = item.id.orDefault(),
                idCompany = item.idCompany.orDefault(),
                idCatagoricalSub = item.idCatagoricalSub.orDefault(),
                mainCatagorical = item.mainCatagorical.orDefault(),
                subCatagoricalName = item.subCatagoricalName.orDefault(),
                name = item.name.orDefault(),
                desc = item.desc.orDefault(),
                idFleet = item.idFleet.orDefault(),
                regNumber = item.regNumber.orDefault(),
                idUser = item.idUser.orDefault(),
                userFirstName = item.userFirstName.orDefault(),
                idCheckSheetDetails = item.idCheckSheetDetails ?: "",
                idStatus = item.idStatus.orDefault(),
                createdOn = item.createdOn.orDefault()
            )
        }
    }
}