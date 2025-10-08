package com.automa.domain.mechanic_task

import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.model.MechanicSubTaskModel
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.domain.mechanic_task.model.MechanicTaskProofModel

interface MechanicTaskRepository {
    suspend fun getMechanicTaskList(id: Int): ResultWrapper<List<MechanicTaskModel>>
    suspend fun getMechanicTaskById(idMechanic: Int, idTask: Int): ResultWrapper<List<MechanicTaskModel>>
    suspend fun getMechanicTaskList(): ResultWrapper<List<MechanicTaskModel>>

    suspend fun getWorkshopTaskList(id: Int): ResultWrapper<List<MechanicTaskModel>>
    suspend fun getWorkshopTaskById(idMechanic: Int, idTask: Int): ResultWrapper<List<MechanicTaskModel>>
    suspend fun getWorkshopTaskList(): ResultWrapper<List<MechanicTaskModel>>

    suspend fun getMechanicSubTaskList(id: Int): ResultWrapper<List<MechanicSubTaskModel>>

    suspend fun createTaskDetail(id: Int, name: String, desc: String, cost: Long, duration: Int): ResultWrapper<String>
    suspend fun updateTaskDetail(idTaskDetail: Int, id: Int, name: String, desc: String, cost: Long, duration: Int): ResultWrapper<String>

    suspend fun uploadTaskProof(file: String, id: Int, name: String, desc: String): ResultWrapper<String>
    suspend fun uploadTaskProofBefore(file: String, id: Int, name: String, desc: String): ResultWrapper<String>
    suspend fun deleteTaskProof(id: Int): ResultWrapper<String>
    suspend fun getListTaskProof(id: Int): ResultWrapper<List<MechanicTaskProofModel>>
    suspend fun getListTaskProofBefore(id: Int): ResultWrapper<List<MechanicTaskProofModel>>
}