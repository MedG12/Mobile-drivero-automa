package com.automa.data.breakdown_report.mapper

import com.automa.data.breakdown_report.model.BreakdownReportPhotoResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.breakdown_report.model.BreakdownReportPhotoModel

class BreakdownReportPhotoMapper: Mapper<List<BreakdownReportPhotoResponse>, List<BreakdownReportPhotoModel>> {
    override fun mapFromResponse(response: List<BreakdownReportPhotoResponse>): List<BreakdownReportPhotoModel> {
        return response.map {
            BreakdownReportPhotoModel(
                id = it.id.orDefault(),
                idCurativeMaintenanceSubmit = it.idCurativeMaintenanceSubmit.orDefault(),
                link = it.link.orDefault(),
                desc = it.desc.orDefault(),
                createdOn = it.createdOn.orDefault()
            )
        }
    }
}