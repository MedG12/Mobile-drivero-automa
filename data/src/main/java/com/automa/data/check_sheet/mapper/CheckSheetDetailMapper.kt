package com.automa.data.check_sheet.mapper

import com.automa.data.check_sheet.model.CheckSheetDetailResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.check_sheet.model.CheckSheetDetailModel

class CheckSheetDetailMapper: Mapper<List<CheckSheetDetailResponse>, List<CheckSheetDetailModel>> {
    override fun mapFromResponse(response: List<CheckSheetDetailResponse>): List<CheckSheetDetailModel> {
        return response.map { item ->
            CheckSheetDetailModel(
                id = item.id.orDefault(),
                desc = item.desc.orDefault(),
                checked = item.checked.orDefault(),
                createdOn = item.createdOn.orDefault(),
                modifiedOn = item.modifiedOn.orDefault(),
                idCheckSheetDo = item.idCheckSheetDo.orDefault(),
                doId = item.doId.orDefault(),
                doNumber = item.doNumber.orDefault(),
                idCheckSheetDetail = item.idCheckSheetDetail.orDefault(),
                notes = item.notes.orDefault(),
                checkSheetDetailActivityName = item.checkSheetDetailActivityName.orDefault(),
                idCheckSheet = item.idCheckSheet.orDefault(),
                checkSheetName = item.checkSheetName.orDefault(),
                checkSheetDesc = item.checkSheetDesc.orDefault()
            )
        }
    }
}