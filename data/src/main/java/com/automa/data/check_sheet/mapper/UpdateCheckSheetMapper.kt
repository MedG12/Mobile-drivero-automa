package com.automa.data.check_sheet.mapper

import com.automa.data.common.Mapper

class UpdateCheckSheetMapper: Mapper<String, String> {
    override fun mapFromResponse(response: String): String {
        return response
    }
}