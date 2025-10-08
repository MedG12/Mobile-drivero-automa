package com.automa.domain.check_sheet.usecase

import com.automa.domain.check_sheet.CheckSheetRepository
import com.automa.domain.check_sheet.model.CheckSheetDetailModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class CheckSheetDetailUseCase @Inject constructor(
    private val repository: CheckSheetRepository
): BaseUseCase<List<CheckSheetDetailModel>>() {
    private var params = mapOf<String, Any>()

    fun addParams(id: Int) = apply {
        params = mapOf(
            ID_CHECK_SHEET to id
        )
    }

    override suspend fun execute(): ResultWrapper<List<CheckSheetDetailModel>> {
        return repository.getCheckSheetDetail(params[ID_CHECK_SHEET] as Int)
    }

    companion object {
        private const val ID_CHECK_SHEET = "ID"
    }
}