package com.automa.domain.live_monitoring

import com.automa.domain.common.ResultWrapper
import com.automa.domain.live_monitoring.model.LiveMonitoringModel

interface LiveMonitoringRepository {
    suspend fun getLiveMonitoring(): ResultWrapper<List<LiveMonitoringModel>>
}