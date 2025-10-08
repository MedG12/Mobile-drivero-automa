package com.automa.data.check_sheet

import com.automa.data.check_sheet.mapper.CheckSheetDetailMapper
import com.automa.data.check_sheet.mapper.CheckSheetMapper
import com.automa.data.check_sheet.mapper.UpdateCheckSheetMapper
import com.automa.data.check_sheet.remote.CheckSheetApiClient
import com.automa.data.common.mapToResult
import com.automa.domain.check_sheet.CheckSheetRepository
import com.automa.domain.check_sheet.model.CheckSheetDetailModel
import com.automa.domain.check_sheet.model.CheckSheetModel
import com.automa.domain.common.ResultWrapper

class CheckSheetRepositoryImpl (
    private val apiClient: CheckSheetApiClient
): CheckSheetRepository {
    override suspend fun getCheckSheet(idDeliveryOrder: Int): ResultWrapper<List<CheckSheetModel>> {
        return try {
            apiClient.getCheckSheet(idDeliveryOrder).mapToResult(CheckSheetMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getCheckSheetDetail(idCheckSheet: Int): ResultWrapper<List<CheckSheetDetailModel>> {
        return try {
            apiClient.getCheckSheetDetail(idCheckSheet).mapToResult(CheckSheetDetailMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun postCheckSheetItem(id: Int, checked: Int, notes: String): ResultWrapper<String> {
        return try {
            apiClient.postCheckSheetItem(id, checked, notes).mapToResult(UpdateCheckSheetMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}