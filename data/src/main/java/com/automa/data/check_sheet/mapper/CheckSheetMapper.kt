package com.automa.data.check_sheet.mapper

import com.automa.data.check_sheet.model.CheckSheetResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.check_sheet.model.CheckSheetModel
class CheckSheetMapper: Mapper<List<CheckSheetResponse>, List<CheckSheetModel>> {
    override fun mapFromResponse(response: List<CheckSheetResponse>): List<CheckSheetModel> {
        return response.map { item ->
            CheckSheetModel(
                id = item.id.orDefault(),
                idCheckSheet = item.idCheckSheet.orDefault(),
                checkSheetName = item.checkSheetName.orDefault(),
                checkSheetDesc = item.checkSheetDesc.orDefault(),
                idDo = item.idDo.orDefault(),
                doNumber = item.doNumber.orDefault(),
                idCheckSheetApprovalType = item.idCheckSheetApprovalType.orDefault(),
                checkSheetApprovalTypeName = item.checkSheetApprovalTypeName.orDefault(),
                createdOn = item.createdOn.orDefault(),
                modifiedOn = item.modifiedOn.orDefault(),
                isApprove = item.isApprove.orDefault()
            )
        }
    }
}

