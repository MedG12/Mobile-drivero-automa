package com.automa.domain.breakdown_report.usecase

import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.breakdown_report.model.BreakdownReportPhotoModel
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class GetBreakdownReportPhotoUseCase @Inject constructor(
    private val repository: BreakdownReportRepository
): BaseUseCase<List<BreakdownReportPhotoModel>>() {
    private var id = -1
    fun addParams(id: Int) = apply {
        this.id = id
    }

    override suspend fun execute(): ResultWrapper<List<BreakdownReportPhotoModel>> {
        return repository.getImage(id)
    }
}