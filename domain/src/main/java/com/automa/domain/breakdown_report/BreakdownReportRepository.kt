package com.automa.domain.breakdown_report

import com.automa.domain.breakdown_report.model.BreakdownCategoryModel
import com.automa.domain.breakdown_report.model.BreakdownReportModel
import com.automa.domain.breakdown_report.model.BreakdownReportPhotoModel
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel
import com.automa.domain.breakdown_report.model.FleetModel
import com.automa.domain.common.ResultWrapper

interface BreakdownReportRepository {
    suspend fun getCategoryList(): ResultWrapper<List<BreakdownCategoryModel>>
    suspend fun getSubCategoryList(idCategorical: Int): ResultWrapper<List<BreakdownSubCategoryModel>>
    suspend fun submitBreakdownReport(idCategorical: Int, name: String, desc: String, idFleet: Int,
                    urgency: Int, isStoring: Boolean, storingReason: String): ResultWrapper<Int>
    suspend fun uploadImage(idCurativeMaintenance: Int, imageDesc: String, file: String): ResultWrapper<String>

    suspend fun getImage(id: Int): ResultWrapper<List<BreakdownReportPhotoModel>>
    suspend fun getBreakdownReports(): ResultWrapper<BreakdownReportModel>
    suspend fun getFleetList(): ResultWrapper<List<FleetModel>>
}