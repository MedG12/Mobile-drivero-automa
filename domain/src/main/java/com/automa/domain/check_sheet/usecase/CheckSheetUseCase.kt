package com.automa.domain.check_sheet.usecase

import com.automa.domain.check_sheet.CheckSheetRepository
import com.automa.domain.check_sheet.model.CheckSheetModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class CheckSheetUseCase @Inject constructor(
    private val repository: CheckSheetRepository
): BaseUseCase<List<CheckSheetModel>>() {
    private var params = mapOf<String, Any>()

    fun addParams(id: Int) = apply {
        params = mapOf(
            ID_DELIVERY_ORDER to id
        )
    }

    override suspend fun execute(): ResultWrapper<List<CheckSheetModel>> {
        return repository.getCheckSheet(params[ID_DELIVERY_ORDER] as Int)
    }

    companion object {
        private const val ID_DELIVERY_ORDER = "ID"
    }
}