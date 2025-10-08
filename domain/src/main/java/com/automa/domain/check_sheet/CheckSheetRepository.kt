package com.automa.domain.check_sheet

import com.automa.domain.check_sheet.model.CheckSheetDetailModel
import com.automa.domain.check_sheet.model.CheckSheetModel
import com.automa.domain.common.ResultWrapper

interface CheckSheetRepository {
    suspend fun getCheckSheet(idDeliveryOrder: Int): ResultWrapper<List<CheckSheetModel>>
    suspend fun getCheckSheetDetail(idCheckSheet: Int): ResultWrapper<List<CheckSheetDetailModel>>
    suspend fun postCheckSheetItem(id: Int, checked: Int, notes: String): ResultWrapper<String>
}