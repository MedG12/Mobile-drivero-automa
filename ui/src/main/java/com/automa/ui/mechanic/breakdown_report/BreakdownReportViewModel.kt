package com.automa.ui.mechanic.breakdown_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.breakdown_report.model.BreakdownCategoryModel
import com.automa.domain.breakdown_report.model.BreakdownReportModel
import com.automa.domain.breakdown_report.model.BreakdownReportPhotoModel
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel
import com.automa.domain.breakdown_report.model.FleetModel
import com.automa.domain.breakdown_report.usecase.*
import com.automa.domain.common.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreakdownReportViewModel @Inject constructor(
    private val categoryUseCase: BreakdownCategoryUseCase,
    private val subCategoryUseCase: BreakdownSubCategoryUseCase,
    private val submitReportUseCase: SubmitBreakdownReportUseCase,
    private val uploadImageBreakdownReportUseCase: UploadImageBreakdownReportUseCase,
    private val getBreakdownReportsUseCase: GetBreakdownReportsUseCase,
    private val fleetListUseCase: FleetListUseCase,
    private val getBreakdownReportPhotoUseCase: GetBreakdownReportPhotoUseCase
): ViewModel() {
    private val _category = MutableLiveData<ResultWrapper<List<BreakdownCategoryModel>>>()
    val category = _category as LiveData<ResultWrapper<List<BreakdownCategoryModel>>>

    private val _subCategory = MutableLiveData<ResultWrapper<List<BreakdownSubCategoryModel>>>()
    val subCategory = _subCategory as LiveData<ResultWrapper<List<BreakdownSubCategoryModel>>>

    private val _submitReport = MutableLiveData<ResultWrapper<Int>>()
    val submitReport = _submitReport as LiveData<ResultWrapper<Int>>

    private val _uploadImage = MutableLiveData<ResultWrapper<String>>()
    val uploadImage = _uploadImage as LiveData<ResultWrapper<String>>

    private val _reports = MutableLiveData<ResultWrapper<BreakdownReportModel>>()
    val reports = _reports as LiveData<ResultWrapper<BreakdownReportModel>>

    private val _fleetList = MutableLiveData<ResultWrapper<List<FleetModel>>>()
    val fleetList = _fleetList as LiveData<ResultWrapper<List<FleetModel>>>

    private val _getPhoto = MutableLiveData<ResultWrapper<List<BreakdownReportPhotoModel>>>()
    val getPhoto = _getPhoto as LiveData<ResultWrapper<List<BreakdownReportPhotoModel>>>

    fun getCategory() {
        viewModelScope.launch {
            _category.value = ResultWrapper.loading()
            categoryUseCase.execute().run(_category::postValue)
        }
    }

    fun getSubCategory(idCategory: Int) {
        viewModelScope.launch {
            _subCategory.value = ResultWrapper.loading()
            subCategoryUseCase.addParams(idCategory).execute().run(_subCategory::postValue)
        }
    }

    fun submitReport(idSubCategory: Int, name: String, desc: String, idFleet: Int, urgency: Int, isStoring: Boolean, storingReason: String) {
        viewModelScope.launch {
            _submitReport.value = ResultWrapper.loading()
            submitReportUseCase.addParams(idSubCategory, name, desc, idFleet, urgency, isStoring, storingReason).execute().run(_submitReport::postValue)
        }
    }

    fun uploadImage(idReport: Int, caption: String, filePath: String) {
        viewModelScope.launch {
            _uploadImage.value = ResultWrapper.loading()
            uploadImageBreakdownReportUseCase.addParams(idReport, caption, filePath).execute().run(_uploadImage::postValue)
        }
    }

    fun getBreakdownReports() {
        viewModelScope.launch {
            _reports.value = ResultWrapper.loading()
            getBreakdownReportsUseCase.execute().run(_reports::postValue)
        }
    }

    fun getFleetList() {
        viewModelScope.launch {
            _fleetList.value = ResultWrapper.loading()
            fleetListUseCase.execute().run(_fleetList::postValue)
        }
    }

    fun getImage(id: Int) {
        viewModelScope.launch {
            _getPhoto.value = ResultWrapper.loading()
            getBreakdownReportPhotoUseCase.addParams(id).execute().run(_getPhoto::postValue)
        }
    }
}