package com.automa.data.mechanic_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.mechanic_task.model.UploadProofResponse

class UploadProofMapper: Mapper<UploadProofResponse, String> {
    override fun mapFromResponse(response: UploadProofResponse): String {
        return response.link.orDefault()
    }
}