package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.MasterDeliveryOrderSubCategoryModel
import javax.inject.Inject

class MasterDeliverySubCategoryUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<List<MasterDeliveryOrderSubCategoryModel>>() {
    private var idCategory = -1

    fun addParams(idCategory: Int) = apply {
        this.idCategory = idCategory
    }

    override suspend fun execute(): ResultWrapper<List<MasterDeliveryOrderSubCategoryModel>> {
        return repository.getMasterDeliveryOrderSubCategory(idCategory)
    }
}