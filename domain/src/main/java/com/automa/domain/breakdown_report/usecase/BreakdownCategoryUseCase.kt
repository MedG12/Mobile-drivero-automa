package com.automa.domain.breakdown_report.usecase

import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.breakdown_report.model.BreakdownCategoryModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class BreakdownCategoryUseCase @Inject constructor(
    private val repository: BreakdownReportRepository
): BaseUseCase<List<BreakdownCategoryModel>>() {
    override suspend fun execute(): ResultWrapper<List<BreakdownCategoryModel>> {
        return repository.getCategoryList()
    }
}