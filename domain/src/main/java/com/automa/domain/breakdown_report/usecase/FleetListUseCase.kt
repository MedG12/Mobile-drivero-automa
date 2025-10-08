package com.automa.domain.breakdown_report.usecase

import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.breakdown_report.model.FleetModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class FleetListUseCase @Inject constructor(
    private val repository: BreakdownReportRepository
): BaseUseCase<List<FleetModel>>() {
    override suspend fun execute(): ResultWrapper<List<FleetModel>> {
        return repository.getFleetList()
    }
}