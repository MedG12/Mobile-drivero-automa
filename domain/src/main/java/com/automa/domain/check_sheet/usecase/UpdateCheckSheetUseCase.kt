package com.automa.domain.check_sheet.usecase

import com.automa.domain.check_sheet.CheckSheetRepository
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class UpdateCheckSheetUseCase @Inject constructor(
    private val repository: CheckSheetRepository
): BaseUseCase<String>() {
    private var params = mapOf<String, Any>()

    fun addParams(id: Int, checked: Int, notes: String) = apply {
        params = mapOf(
            ID_ITEM to id,
            CHECKED to checked,
            NOTES to notes
        )
    }

    override suspend fun execute(): ResultWrapper<String> {
        return repository.postCheckSheetItem(
            params[ID_ITEM] as Int,
            params[CHECKED] as Int,
            params[NOTES] as String
        )
    }

    companion object {
        private const val ID_ITEM = "ID"
        private const val CHECKED = "CHECKED"
        private const val NOTES = "NOTES"
    }
}