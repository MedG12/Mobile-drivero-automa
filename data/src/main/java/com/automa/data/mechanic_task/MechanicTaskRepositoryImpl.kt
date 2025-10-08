package com.automa.data.mechanic_task

import com.automa.data.common.Mapper
import com.automa.data.common.mapToResult
import com.automa.data.mechanic_task.mapper.MechanicSubTaskMapper
import com.automa.data.mechanic_task.mapper.MechanicTaskListMapper
import com.automa.data.mechanic_task.mapper.MechanicTaskProofMapper
import com.automa.data.mechanic_task.mapper.UploadProofMapper
import com.automa.data.mechanic_task.remote.MechanicTaskApiClient
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.MechanicTaskRepository
import com.automa.domain.mechanic_task.model.MechanicSubTaskModel
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.domain.mechanic_task.model.MechanicTaskProofModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MechanicTaskRepositoryImpl @Inject constructor(
    private val apiClient: MechanicTaskApiClient
): MechanicTaskRepository {
    override suspend fun getMechanicTaskList(id: Int): ResultWrapper<List<MechanicTaskModel>> {
        return try {
            val body = hashMapOf<String, Any?>()
            body["id_mechanic"] = id
            apiClient.getMechanicTaskList(body).mapToResult(MechanicTaskListMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMechanicTaskById(idMechanic: Int, idTask: Int): ResultWrapper<List<MechanicTaskModel>> {
        return try {
            val body = hashMapOf<String, Any?>()
            body["id_mechanic"] = idMechanic
            body["id"] = idTask
            apiClient.getMechanicTaskList(body).mapToResult(MechanicTaskListMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMechanicTaskList(): ResultWrapper<List<MechanicTaskModel>> {
        return try {
            apiClient.getMechanicTaskList().mapToResult(MechanicTaskListMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getWorkshopTaskList(id: Int): ResultWrapper<List<MechanicTaskModel>> {
        return try {
            val body = hashMapOf<String, Any?>()
            body["id_mechanic"] = id
            apiClient.getWorkshopTaskList(body).mapToResult(MechanicTaskListMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getWorkshopTaskById(idMechanic: Int, idTask: Int): ResultWrapper<List<MechanicTaskModel>> {
        return try {
            val body = hashMapOf<String, Any?>()
            body["id_mechanic"] = idMechanic
            body["id"] = idTask
            apiClient.getWorkshopTaskList(body).mapToResult(MechanicTaskListMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getWorkshopTaskList(): ResultWrapper<List<MechanicTaskModel>> {
        return try {
            apiClient.getWorkshopTaskList().mapToResult(MechanicTaskListMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMechanicSubTaskList(id: Int): ResultWrapper<List<MechanicSubTaskModel>> {
        return try {
            apiClient.getSubTaskList(id).mapToResult(MechanicSubTaskMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun createTaskDetail(
        id: Int,
        name: String,
        desc: String,
        cost: Long,
        duration: Int
    ): ResultWrapper<String> {
        return try {
            apiClient.createTaskDetail(id, name, desc, cost, duration).mapToResult(object : Mapper<HashMap<String, Any>, String> {
                override fun mapFromResponse(response: HashMap<String, Any>): String {
                    return response["id"].toString()
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun updateTaskDetail(
        idTaskDetail: Int,
        id: Int,
        name: String,
        desc: String,
        cost: Long,
        duration: Int
    ): ResultWrapper<String> {
        return try {
            apiClient.updateTaskDetail(idTaskDetail, id, name, desc, cost, duration).mapToResult(object : Mapper<String, String> {
                override fun mapFromResponse(response: String): String {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun uploadTaskProof(
        file: String,
        id: Int,
        name: String,
        desc: String
    ): ResultWrapper<String> {
        return try {
            val imgFile = File(file)
            val requestFile = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val partImage = MultipartBody.Part.createFormData("file", imgFile.name, requestFile)
            val map = mapOf(
                "image_desc" to name.toRequestBody("multipart/form-data".toMediaType()),
            )
            val result = apiClient.uploadPod(partImage, map).mapToResult(UploadProofMapper())
            if (result is ResultWrapper.Success) {
                inputTaskProof(id, result.data, name, desc)
            } else {
                result
            }
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun uploadTaskProofBefore(
        file: String,
        id: Int,
        name: String,
        desc: String
    ): ResultWrapper<String> {
        return try {
            val imgFile = File(file)
            val requestFile = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val partImage = MultipartBody.Part.createFormData("file", imgFile.name, requestFile)
            val map = mapOf(
                "image_desc" to name.toRequestBody("multipart/form-data".toMediaType()),
            )
            val result = apiClient.uploadPod(partImage, map).mapToResult(UploadProofMapper())
            if (result is ResultWrapper.Success) {
                inputTaskProofBefore(id, result.data, name, desc)
            } else {
                result
            }
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    private suspend fun inputTaskProof(
        id: Int,
        link: String,
        name: String,
        desc: String
    ): ResultWrapper<String> {
        return try {
            apiClient.inputTaskProof(id, link, name, desc).mapToResult(object : Mapper<HashMap<String, Any>, String> {
                override fun mapFromResponse(response: HashMap<String, Any>): String {
                    return try {
                        response["id"] as String
                    } catch (e: Exception) { "-1" }
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    private suspend fun inputTaskProofBefore(
        id: Int,
        link: String,
        name: String,
        desc: String
    ): ResultWrapper<String> {
        return try {
            apiClient.inputTaskProofBefore(id, link, name, desc).mapToResult(object : Mapper<HashMap<String, Any>, String> {
                override fun mapFromResponse(response: HashMap<String, Any>): String {
                    return try {
                        response["id"] as String
                    } catch (e: Exception) { "-1" }
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun deleteTaskProof(id: Int): ResultWrapper<String> {
        return try {
            apiClient.deleteTaskProof(id).mapToResult(object : Mapper<String, String> {
                override fun mapFromResponse(response: String): String {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getListTaskProof(id: Int): ResultWrapper<List<MechanicTaskProofModel>> {
        return try {
            apiClient.getListTaskProof(id).mapToResult(MechanicTaskProofMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getListTaskProofBefore(id: Int): ResultWrapper<List<MechanicTaskProofModel>> {
        return try {
            apiClient.getListTaskProofBefore(id).mapToResult(MechanicTaskProofMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}