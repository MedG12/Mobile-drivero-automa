package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.MasterDeliveryOrderItemModel
import javax.inject.Inject

class MasterDeliveryOrderUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<List<MasterDeliveryOrderItemModel>>() {
    private var idCategory = -1
    private var idSubCategory = -1
    fun addParams(idCategory: Int, idSubCategory: Int) = apply {
        this.idCategory = idCategory
        this.idSubCategory = idSubCategory
    }
    override suspend fun execute(): ResultWrapper<List<MasterDeliveryOrderItemModel>> {
        return repository.getMasterDeliveryOrderList(idCategory, idSubCategory)
    }
}