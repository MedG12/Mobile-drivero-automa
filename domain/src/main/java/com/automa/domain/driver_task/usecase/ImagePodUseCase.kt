package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.driver_task.model.ImagePodModel
import javax.inject.Inject

class ImagePodUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<ImagePodModel>() {
    private var params = mapOf<String, Any>()

    fun addParams(idDeliveryOrder: Int, idWorkOrder: Int) = apply {
        params = mapOf(
            ID_WORK_ORDER to idWorkOrder,
            ID_DELIVERY_ORDER to idDeliveryOrder
        )
    }

    override suspend fun execute(): ResultWrapper<ImagePodModel> {
        return repository.getImageList(
            params[ID_DELIVERY_ORDER] as Int,
            params[ID_WORK_ORDER] as Int
        )
    }

    companion object {
        private const val ID_DELIVERY_ORDER = "ID_DO"
        private const val ID_WORK_ORDER = "ID_WO"
    }
}