package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.ImagePodResponse
import com.automa.domain.driver_task.model.ImagePodItemModel
import com.automa.domain.driver_task.model.ImagePodModel

class ImagePodMapper: Mapper<ImagePodResponse, ImagePodModel> {
    override fun mapFromResponse(response: ImagePodResponse): ImagePodModel {
        return ImagePodModel(
            result = response.result?.map { item ->
                ImagePodItemModel(
                    id = item.id.orDefault(),
                    idWorkOrder = item.idWorkOrder.orDefault(),
                    link = item.link.orDefault(),
                    desc = item.desc.orDefault(),
                    createdOn = item.createdOn.orDefault()
                )
            } ?: emptyList()
        )
    }
}