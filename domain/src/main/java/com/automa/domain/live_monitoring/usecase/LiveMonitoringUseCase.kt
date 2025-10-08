package com.automa.domain.live_monitoring.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.live_monitoring.LiveMonitoringRepository
import com.automa.domain.live_monitoring.model.LiveMonitoringModel
import javax.inject.Inject

class LiveMonitoringUseCase @Inject constructor(
    private val repository: LiveMonitoringRepository
): BaseUseCase<List<LiveMonitoringModel>>() {
    override suspend fun execute(): ResultWrapper<List<LiveMonitoringModel>> {
        return repository.getLiveMonitoring()
    }
}