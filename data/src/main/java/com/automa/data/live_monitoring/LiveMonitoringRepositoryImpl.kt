package com.automa.data.live_monitoring

import com.automa.data.common.mapToResult
import com.automa.data.live_monitoring.mapper.LiveMonitoringMapper
import com.automa.data.live_monitoring.remote.LiveMonitoringApiClient
import com.automa.domain.common.ResultWrapper
import com.automa.domain.live_monitoring.LiveMonitoringRepository
import com.automa.domain.live_monitoring.model.LiveMonitoringModel

class LiveMonitoringRepositoryImpl(
    private val api: LiveMonitoringApiClient
): LiveMonitoringRepository {
    override suspend fun getLiveMonitoring(): ResultWrapper<List<LiveMonitoringModel>> {
        return try {
            api.getLiveMonitoring().mapToResult(LiveMonitoringMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}