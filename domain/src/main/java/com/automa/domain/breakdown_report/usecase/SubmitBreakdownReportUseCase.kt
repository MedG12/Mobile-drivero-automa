package com.automa.domain.breakdown_report.usecase

import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import javax.inject.Inject

class SubmitBreakdownReportUseCase @Inject constructor(
    private val repository: BreakdownReportRepository
): BaseUseCase<Int>() {
    private var idSub = 0
    private var name = ""
    private var desc = ""
    private var idFleet = 0
    private var urgency = 0
    private var isStoring = false
    private var storingReason = ""

    fun addParams(
        idSub: Int,
        name: String,
        desc: String,
        idFleet: Int,
        urgency: Int,
        isStoring: Boolean,
        storingReason: String
    ) = apply {
        this.idSub = idSub
        this.name = name
        this.desc = desc
        this.idFleet = idFleet
        this.urgency = urgency
        this.isStoring = isStoring
        this.storingReason = storingReason
    }

    override suspend fun execute(): ResultWrapper<Int> {
        return repository.submitBreakdownReport(idSub, name, desc, idFleet, urgency, isStoring, storingReason)
    }
}