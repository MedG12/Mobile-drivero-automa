package com.automa.data.breakdown_report

import com.automa.data.breakdown_report.mapper.BreakdownCategoryMapper
import com.automa.data.breakdown_report.mapper.BreakdownReportMapper
import com.automa.data.breakdown_report.mapper.BreakdownReportPhotoMapper
import com.automa.data.breakdown_report.mapper.BreakdownSubCategoryMapper
import com.automa.data.breakdown_report.mapper.FleetMapper
import com.automa.data.breakdown_report.remote.BreakdownReportApiClient
import com.automa.data.common.Mapper
import com.automa.data.common.mapToResult
import com.automa.data.driver_task.mapper.UploadPodMapper
import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.breakdown_report.model.BreakdownCategoryModel
import com.automa.domain.breakdown_report.model.BreakdownReportModel
import com.automa.domain.breakdown_report.model.BreakdownReportPhotoModel
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel
import com.automa.domain.breakdown_report.model.FleetModel
import com.automa.domain.common.ResultWrapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class BreakdownReportRepositoryImpl(
    private val apiClient: BreakdownReportApiClient
): BreakdownReportRepository {
    override suspend fun getCategoryList(): ResultWrapper<List<BreakdownCategoryModel>> {
        return try {
            apiClient.getCategoryList().mapToResult(BreakdownCategoryMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getSubCategoryList(idCategorical: Int): ResultWrapper<List<BreakdownSubCategoryModel>> {
        return try {
            apiClient.getSubCategoryList().mapToResult(BreakdownSubCategoryMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun submitBreakdownReport(
        idCategorical: Int,
        name: String,
        desc: String,
        idFleet: Int,
        urgency: Int,
        isStoring: Boolean,
        storingReason: String
    ): ResultWrapper<Int> {
        val isStoringInt = if (isStoring) 1 else 0
        return try {
            apiClient.submitBreakdownReport(
                idCategorical, name, desc, idFleet, urgency, isStoringInt, storingReason
            ).mapToResult(object : Mapper<HashMap<String, Any>, Int> {
                override fun mapFromResponse(response: HashMap<String, Any>): Int {
                    return try {
                        (response["id"] as Double).toInt()
                    } catch (e: Exception) {
                        -1
                    }
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun uploadImage(
        idCurativeMaintenance: Int,
        imageDesc: String,
        file: String
    ): ResultWrapper<String> {
        return try {
            val imgFile = File(file)
            val requestFile = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val partImage = MultipartBody.Part.createFormData("file", imgFile.name, requestFile)
            val map = mapOf(
                "id_curative_maintenance_submit" to idCurativeMaintenance.toString().toRequestBody("multipart/form-data".toMediaType()),
                "image_desc" to imageDesc.toRequestBody("multipart/form-data".toMediaType()),
            )
            apiClient.uploadImage(partImage, map).mapToResult(UploadPodMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getImage(id: Int): ResultWrapper<List<BreakdownReportPhotoModel>> {
        return try {
            apiClient.getImage(id).mapToResult(BreakdownReportPhotoMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getBreakdownReports(): ResultWrapper<BreakdownReportModel> {
        return try {
            apiClient.getBreakdownReports().mapToResult(BreakdownReportMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getFleetList(): ResultWrapper<List<FleetModel>> {
        return try {
            apiClient.getFleetList().mapToResult(FleetMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}