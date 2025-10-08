package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.UploadPodResponse

class UploadPodMapper: Mapper<UploadPodResponse, String> {
    override fun mapFromResponse(response: UploadPodResponse): String {
        return response.uploadResult.link.orDefault()
    }
}