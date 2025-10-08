package com.automa.data.breakdown_report.mapper

import com.automa.data.breakdown_report.model.BreakdownSubCategoryResponse
import com.automa.data.common.Mapper
import com.automa.data.common.orDefault
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel

class BreakdownSubCategoryMapper: Mapper<List<BreakdownSubCategoryResponse>, List<BreakdownSubCategoryModel>> {
    override fun mapFromResponse(response: List<BreakdownSubCategoryResponse>): List<BreakdownSubCategoryModel> {
        return response.map { item ->
            BreakdownSubCategoryModel(
                id = item.id.orDefault(),
                idCategory = item.idCategory.orDefault(),
                name = item.name.orDefault(),
                desc = item.desc.orDefault()
            )
        }
    }
}