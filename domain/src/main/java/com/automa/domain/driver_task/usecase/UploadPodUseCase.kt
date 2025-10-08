package com.automa.domain.driver_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.DriverTaskRepository
import javax.inject.Inject

class UploadPodUseCase @Inject constructor(
    private val repository: DriverTaskRepository
): BaseUseCase<String>() {
    private var params = mapOf<String, Any>()

    fun addParams(idDeliveryOrder: Int, idWorkOrder: Int, imageDesc: String, file: String) = apply {
        params = mapOf(
            ID_DELIVERY_ORDER to idDeliveryOrder,
            ID_WORK_ORDER to idWorkOrder,
            IMAGE_DESC to imageDesc,
            FILE to file
        )
    }

    override suspend fun execute(): ResultWrapper<String> {
        return repository.uploadPod(
            params[ID_DELIVERY_ORDER] as Int,
            params[ID_WORK_ORDER] as Int,
            params[IMAGE_DESC] as String,
            params[FILE] as String
        )
    }

    companion object {
        private const val ID_DELIVERY_ORDER = "ID_DO"
        private const val ID_WORK_ORDER = "ID_WO"
        private const val FILE = "FILE"
        private const val IMAGE_DESC = "DESC"
    }
}