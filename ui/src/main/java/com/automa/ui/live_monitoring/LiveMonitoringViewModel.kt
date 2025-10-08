package com.automa.ui.live_monitoring

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.common.ResultWrapper
import com.automa.domain.live_monitoring.model.LiveMonitoringModel
import com.automa.domain.live_monitoring.usecase.LiveMonitoringUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveMonitoringViewModel @Inject constructor(
    private val liveMonitoringUseCase: LiveMonitoringUseCase
): ViewModel() {
    private val _liveMonitoring = MutableLiveData<ResultWrapper<List<LiveMonitoringModel>>>()
    val liveMonitoring = _liveMonitoring as LiveData<ResultWrapper<List<LiveMonitoringModel>>>

    fun getLiveMonitoring() {
        _liveMonitoring.value = ResultWrapper.loading()
        viewModelScope.launch {
            liveMonitoringUseCase.execute().run(_liveMonitoring::postValue)
        }
    }
}