package com.automa.data.driver_task.mapper

import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.data.driver_task.model.MasterDeliveryOrderCategoryResponse
import com.automa.domain.driver_task.model.MasterDeliveryOrderCategoryModel

class MasterDeliveryOrderCategoryMapper:
    Mapper<List<MasterDeliveryOrderCategoryResponse>,
            List<MasterDeliveryOrderCategoryModel>> {
    override fun mapFromResponse(response: List<MasterDeliveryOrderCategoryResponse>): List<MasterDeliveryOrderCategoryModel> {
        return response.map {
            MasterDeliveryOrderCategoryModel(
                id = it.id.orDefault(),
                idCompany = it.id.orDefault(),
                name = it.name.orDefault()
            )
        }
    }
}