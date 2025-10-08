package com.automa.domain.breakdown_report.usecase

import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.breakdown_report.model.BreakdownReportModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class GetBreakdownReportsUseCase @Inject constructor(
    private val repository: BreakdownReportRepository
): BaseUseCase<BreakdownReportModel>() {
    override suspend fun execute(): ResultWrapper<BreakdownReportModel> {
        return repository.getBreakdownReports()
    }
}