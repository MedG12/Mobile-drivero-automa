package com.automa.ui.driver.check_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.check_sheet.model.CheckSheetDetailModel
import com.automa.domain.check_sheet.model.CheckSheetModel
import com.automa.domain.check_sheet.usecase.CheckSheetDetailUseCase
import com.automa.domain.check_sheet.usecase.CheckSheetUseCase
import com.automa.domain.check_sheet.usecase.UpdateCheckSheetUseCase
import com.automa.domain.common.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckSheetViewModel @Inject constructor(
    private val checkSheetUseCase: CheckSheetUseCase,
    private val checkSheetDetailUseCase: CheckSheetDetailUseCase,
    private val updateCheckSheetUseCase: UpdateCheckSheetUseCase
) : ViewModel() {
    private val _checkSheet = MutableLiveData<ResultWrapper<List<CheckSheetModel>>>()
    val checkSheet = _checkSheet as LiveData<ResultWrapper<List<CheckSheetModel>>>

    private val _checkSheetDetail = MutableLiveData<ResultWrapper<List<CheckSheetDetailModel>>>()
    val checkSheetDetail = _checkSheetDetail as LiveData<ResultWrapper<List<CheckSheetDetailModel>>>

    private val _updateCheckSheetItem = MutableLiveData<ResultWrapper<Pair<Int, List<Triple<Int, Int, String>>>>>()
    val updateCheckSheetItem = _updateCheckSheetItem as LiveData<ResultWrapper<Pair<Int, List<Triple<Int, Int, String>>>>>

    fun getCheckSheet(idDeliveryOrder: Int) {
        _checkSheet.value = ResultWrapper.loading()
        viewModelScope.launch {
            checkSheetUseCase.addParams(idDeliveryOrder).execute().run(_checkSheet::postValue)
        }
    }

    fun getCheckSheetDetail(idCheckSheet: Int) {
        _checkSheetDetail.value = ResultWrapper.loading()
        viewModelScope.launch {
            checkSheetDetailUseCase.addParams(idCheckSheet).execute().run(_checkSheetDetail::postValue)
        }
    }

    fun updateCheckSheetItem(list: List<Triple<Int, Int, String>>) {
        _updateCheckSheetItem.value = ResultWrapper.loading()
        var successCount = 0
        val failData = mutableListOf<Triple<Int, Int, String>>()
        viewModelScope.launch {
            list.forEach { item ->
                updateCheckSheetUseCase.addParams(item.first, item.second, item.third).execute().run {
                    if (this is ResultWrapper.Success) {
                        successCount++
                    } else {
                        failData.add(item)
                    }
                }
            }
            _updateCheckSheetItem.postValue(ResultWrapper.success(Pair(successCount, failData)))
        }
    }
}