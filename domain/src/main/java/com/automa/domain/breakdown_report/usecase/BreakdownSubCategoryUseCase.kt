package com.automa.domain.breakdown_report.usecase

import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class BreakdownSubCategoryUseCase @Inject constructor(
    private val repository: BreakdownReportRepository
): BaseUseCase<List<BreakdownSubCategoryModel>>() {
    private var idCategorical = 0

    fun addParams(idCategorical: Int) = apply {
        this.idCategorical = idCategorical
    }

    override suspend fun execute(): ResultWrapper<List<BreakdownSubCategoryModel>> {
        return repository.getSubCategoryList(idCategorical)
    }
}