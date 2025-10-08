package com.automa.ui.driver.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.common.ResultWrapper
import com.automa.domain.driver_task.model.ImagePodModel
import com.automa.domain.driver_task.model.TaskDetailModel
import com.automa.domain.driver_task.usecase.ImagePodUseCase
import com.automa.domain.driver_task.usecase.PostTaskDoneUseCase
import com.automa.domain.driver_task.usecase.TaskDetailUseCase
import com.automa.domain.driver_task.usecase.UploadPodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskDetailUseCase: TaskDetailUseCase,
    private val uploadPodUseCase: UploadPodUseCase,
    private val imagePodUseCase: ImagePodUseCase,
    private val taskDoneUseCase: PostTaskDoneUseCase
) : ViewModel() {
    private val _taskDetail = MutableLiveData<ResultWrapper<TaskDetailModel>>()
    val taskDetail = _taskDetail as LiveData<ResultWrapper<TaskDetailModel>>

    private val _uploadPod = MutableLiveData<ResultWrapper<String>>()
    val uploadPodResult = _uploadPod as LiveData<ResultWrapper<String>>

    private val _listImage = MutableLiveData<List<ResultWrapper<ImagePodModel>>>()
    val listImage = _listImage as LiveData<List<ResultWrapper<ImagePodModel>>>

    private val _taskDetailRefresh = MutableLiveData<ResultWrapper<TaskDetailModel>>()
    val taskDetailRefresh = _taskDetailRefresh as LiveData<ResultWrapper<TaskDetailModel>>

    private val _checkCompletion = MutableLiveData<ResultWrapper<TaskDetailModel>>()
    val checkCompletion = _checkCompletion as LiveData<ResultWrapper<TaskDetailModel>>

    private val _postTaskDone = MutableLiveData<ResultWrapper<Int>>()
    val postTaskDone = _postTaskDone as LiveData<ResultWrapper<Int>>

    fun getTaskDetail(id: Int) {
        _taskDetail.value = ResultWrapper.loading()
        viewModelScope.launch {
            taskDetailUseCase.addParams(id).execute().run(_taskDetail::postValue)
        }
    }

    fun getTaskDetailRefresh(id: Int) {
        _taskDetailRefresh.value = ResultWrapper.loading()
        viewModelScope.launch {
            taskDetailUseCase.addParams(id).execute().run(_taskDetailRefresh::postValue)
        }
    }

    fun checkTaskDetailCompletion(id: Int) {
        _checkCompletion.value = ResultWrapper.loading()
        viewModelScope.launch {
            taskDetailUseCase.addParams(id).execute().run(_checkCompletion::postValue)
        }
    }

    fun uploadPod(idDeliveryOrder: Int, idWorkOrder: Int, imageDesc: String, imageFile: String) {
        _uploadPod.value = ResultWrapper.loading()
        viewModelScope.launch {
            uploadPodUseCase.addParams(idDeliveryOrder, idWorkOrder, imageDesc, imageFile).execute().run(_uploadPod::postValue)
        }
    }

    fun getListImage(idDeliveryOrder: Int, idWorkOrder: List<Int>) {
        val result = mutableListOf<ResultWrapper<ImagePodModel>>()
        viewModelScope.launch {
            idWorkOrder.forEach {
                imagePodUseCase.addParams(idDeliveryOrder, it).execute().run { result.add(this) }
            }
            _listImage.postValue(result)
        }
    }

    fun postTaskDone(idDeliveryOrder: Int) {
        _postTaskDone.value = ResultWrapper.loading()
        viewModelScope.launch {
            taskDoneUseCase.addParams(idDeliveryOrder).execute().run(_postTaskDone::postValue)
        }
    }
}