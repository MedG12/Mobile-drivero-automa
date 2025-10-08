package com.automa.domain.breakdown_report.usecase

import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class UploadImageBreakdownReportUseCase @Inject constructor(
    private val repository: BreakdownReportRepository
): BaseUseCase<String>() {
    private var idReport = 0
    private var caption = ""
    private var filePath = ""

    fun addParams(
        idReport: Int,
        caption: String,
        file: String
    ) = apply {
        this.idReport = idReport
        this.caption = caption
        this.filePath = file
    }

    override suspend fun execute(): ResultWrapper<String> {
        return repository.uploadImage(idReport, caption, filePath)
    }
}