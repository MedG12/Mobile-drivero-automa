package com.automa.ui.driver.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.domain.driver_task.model.MasterDeliveryOrderCategoryModel
import com.automa.domain.driver_task.model.MasterDeliveryOrderItemModel
import com.automa.domain.driver_task.model.MasterDeliveryOrderSubCategoryModel
import com.automa.domain.driver_task.model.WorkOrderModel
import com.automa.domain.driver_task.usecase.DeliveryOrderByIdUseCase
import com.automa.domain.driver_task.usecase.DeliveryOrderUseCase
import com.automa.domain.driver_task.usecase.DuplicateDeliveryOrderUseCase
import com.automa.domain.driver_task.usecase.MasterDeliveryCategoryUseCase
import com.automa.domain.driver_task.usecase.MasterDeliveryOrderUseCase
import com.automa.domain.driver_task.usecase.MasterDeliverySubCategoryUseCase
import com.automa.domain.driver_task.usecase.WorkOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deliveryOrderUseCase: DeliveryOrderUseCase,
    private val deliveryOrderByIdUseCase: DeliveryOrderByIdUseCase,
    private val workOrderUseCase: WorkOrderUseCase,
    private val duplicateDeliveryOrderUseCase: DuplicateDeliveryOrderUseCase,
    private val masterDeliveryOrderUseCase: MasterDeliveryOrderUseCase,
    private val masterDeliveryCategoryUseCase: MasterDeliveryCategoryUseCase,
    private val masterDeliverySubCategoryUseCase: MasterDeliverySubCategoryUseCase
) : ViewModel() {
    private val _deliveryOrderList = MutableLiveData<ResultWrapper<List<DeliveryOrderModel>>>()
    val deliveryOrderList = _deliveryOrderList as LiveData<ResultWrapper<List<DeliveryOrderModel>>>

    private val _deliveryOrderByIdList = MutableLiveData<ResultWrapper<List<DeliveryOrderModel>>>()
    val deliveryOrderByIdList = _deliveryOrderByIdList as LiveData<ResultWrapper<List<DeliveryOrderModel>>>

    private val _workOrder = MutableLiveData<ResultWrapper<WorkOrderModel>>()
    val workOrder = _workOrder as LiveData<ResultWrapper<WorkOrderModel>>

    private val _getDuplicateWorkOrder = MutableLiveData<ResultWrapper<WorkOrderModel>>()
    val getDuplicateWorkOrder = _getDuplicateWorkOrder as LiveData<ResultWrapper<WorkOrderModel>>

    private val _postDuplicateDeliveryOrder = MutableLiveData<ResultWrapper<Int>>()
    val postDuplicateDeliveryOrder = _postDuplicateDeliveryOrder as LiveData<ResultWrapper<Int>>

    private val _masterDeliveryOrderList = MutableLiveData<ResultWrapper<List<MasterDeliveryOrderItemModel>>>()
    val masterDeliveryOrderList = _masterDeliveryOrderList as LiveData<ResultWrapper<List<MasterDeliveryOrderItemModel>>>

    private val _masterDeliveryOrderCategory = MutableLiveData<ResultWrapper<List<MasterDeliveryOrderCategoryModel>>>()
    val masterDeliveryOrderCategory = _masterDeliveryOrderCategory as LiveData<ResultWrapper<List<MasterDeliveryOrderCategoryModel>>>

    private val _masterDeliveryOrderSubCategory = MutableLiveData<ResultWrapper<List<MasterDeliveryOrderSubCategoryModel>>>()
    val masterDeliveryOrderSubCategory = _masterDeliveryOrderSubCategory as LiveData<ResultWrapper<List<MasterDeliveryOrderSubCategoryModel>>>

    fun getDeliveryOrder(day: Int, driverId: Int, fleetId: Int = -1, driverDone: Int ?= null) {
        _deliveryOrderList.value = ResultWrapper.loading()
        viewModelScope.launch {
            deliveryOrderUseCase.addParams(day, driverId, fleetId, driverDone).execute().run(_deliveryOrderList::postValue)
        }
    }

    fun getDeliveryOrderById(id: Int) {
        _deliveryOrderByIdList.value = ResultWrapper.loading()
        viewModelScope.launch {
            deliveryOrderByIdUseCase.addParams(id).execute().run(_deliveryOrderByIdList::postValue)
        }
    }

    fun getWorkOrder(id: Int) {
        _workOrder.value = ResultWrapper.loading()
        viewModelScope.launch {
            workOrderUseCase.addParams(id).execute().run(_workOrder::postValue)
        }
    }

    fun getDuplicateWorkOrder(id: Int) {
        _getDuplicateWorkOrder.value = ResultWrapper.loading()
        viewModelScope.launch {
            workOrderUseCase.addParams(id).execute().run(_getDuplicateWorkOrder::postValue)
        }
    }

    fun postDuplicateDeliveryOrder(idDo: Int, assignedDate: String, estDepartureTime: String, endAssignedDate: String, idFleet: Int, idDriver: Int) {
        _postDuplicateDeliveryOrder.value = ResultWrapper.loading()
        viewModelScope.launch {
            duplicateDeliveryOrderUseCase.addParams(
                idDo, assignedDate, estDepartureTime, endAssignedDate, idFleet, idDriver
            ).execute().run(_postDuplicateDeliveryOrder::postValue)
        }
    }

    fun getMasterDeliveryOrder(idCategory: Int, idSubCategory: Int) {
        _masterDeliveryOrderList.value = ResultWrapper.loading()
        viewModelScope.launch {
            masterDeliveryOrderUseCase.addParams(idCategory, idSubCategory).execute().run(_masterDeliveryOrderList::postValue)
        }
    }

    fun getMasterDeliveryOrderCategory() {
        _masterDeliveryOrderCategory.value = ResultWrapper.loading()
        viewModelScope.launch {
            masterDeliveryCategoryUseCase.execute().run(_masterDeliveryOrderCategory::postValue)
        }
    }

    fun getMasterDeliveryOrderSubCategory(idCategory: Int) {
        _masterDeliveryOrderSubCategory.value = ResultWrapper.loading()
        viewModelScope.launch {
            masterDeliverySubCategoryUseCase.addParams(idCategory).execute().run(_masterDeliveryOrderSubCategory::postValue)
        }
    }
}