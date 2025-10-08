package com.automa.domain.mechanic_task.usecase

import com.automa.domain.common.BaseUseCase
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import javax.inject.Inject

class MechanicTaskDetailUploadProofUseCase @Inject constructor(
    private val repository: MechanicTaskRepository
): BaseUseCase<String>() {
    private var params = mapOf<String, Any>()

    fun addParams(file: String, id: Int, name: String, desc: String) = apply {
        params = mapOf(
            TASK_ID to id,
            NAME to name,
            IMAGE_DESC to desc,
            FILE to file
        )
    }

    override suspend fun execute(): ResultWrapper<String> {
        return repository.uploadTaskProof(
            params[FILE] as String,
            params[TASK_ID] as Int,
            params[NAME] as String,
            params[IMAGE_DESC] as String
        )
    }

    companion object {
        private const val FILE = "FILE"
        private const val TASK_ID = "ID"
        private const val NAME = "NAME"
        private const val IMAGE_DESC = "DESC"
    }
}