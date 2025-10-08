package com.automa.data.live_monitoring.remote

import com.automa.data.common.IResponse
import com.automa.data.live_monitoring.model.LiveMonitoringResponse
import retrofit2.http.POST

interface LiveMonitoringApiClient {
    @POST("transporter/report/live")
    suspend fun getLiveMonitoring(): IResponse<List<LiveMonitoringResponse>>
}