package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.MasterDeliveryOrderSubCategoryResponse
import com.automa.domain.driver_task.model.MasterDeliveryOrderSubCategoryModel

class MasterDeliveryOrderSubCategoryMapper: Mapper<List<MasterDeliveryOrderSubCategoryResponse>,
        List<MasterDeliveryOrderSubCategoryModel>> {
    override fun mapFromResponse(response: List<MasterDeliveryOrderSubCategoryResponse>): List<MasterDeliveryOrderSubCategoryModel> {
        return response.map {
            MasterDeliveryOrderSubCategoryModel(
                id = it.id.orDefault(),
                idDeliveryOrderCategory = it.idDeliveryOrderCategory.orDefault(),
                name = it.name.orDefault()
            )
        }
    }
}