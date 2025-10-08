package com.automa.data.breakdown_report.mapper

import com.automa.data.breakdown_report.model.BreakdownReportResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.breakdown_report.model.BreakdownReportItemModel
import com.automa.domain.breakdown_report.model.BreakdownReportModel

class BreakdownReportMapper: Mapper<BreakdownReportResponse, BreakdownReportModel> {
    override fun mapFromResponse(response: BreakdownReportResponse): BreakdownReportModel {
        return BreakdownReportModel(
            data = response.data?.map { item ->
                BreakdownReportItemModel(
                    id = item.id.orDefault(),
                    idCompany = item.idCompany.orDefault(),
                    idCategoricalSub = item.idCatagoricalSub.orDefault(),
                    mainCategorical = item.mainCatagorical.orDefault(),
                    subCategoricalName = item.subCatagoricalName.orDefault(),
                    name = item.name.orDefault(),
                    desc = item.desc.orDefault(),
                    idFleet = item.idFleet.orDefault(),
                    regNumber = item.regNumber.orDefault(),
                    idUser = item.idUser.orDefault(),
                    userFirstName = item.userFirstName.orDefault(),
                    idCheckSheetDetails = item.idCheckSheetDetails.orDefault(),
                    idStatus = item.idStatus.orDefault(),
                    createdOn = item.createdOn.orDefault(),
                    level = item.level.orDefault(),
                    isStoring = item.isStoring == 1,
                    storingReason = item.storingReason.orDefault()
                )
            } ?: emptyList()
        )
    }
}