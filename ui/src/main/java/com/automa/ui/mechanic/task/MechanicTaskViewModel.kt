package com.automa.ui.mechanic.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automa.domain.common.ResultWrapper
import com.automa.domain.mechanic_task.model.MechanicSubTaskModel
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.domain.mechanic_task.model.MechanicTaskProofModel
import com.automa.domain.mechanic_task.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MechanicTaskViewModel @Inject constructor(
    private val mechanicTaskUseCase: MechanicTaskListUseCase,
    private val headMechanicTaskUseCase: HeadMechanicTaskListUseCase,
    private val mechanicTaskByIdUseCase: MechanicTaskByIdUseCase,
    private val workshopTaskListUseCase: MechanicWorkshopTaskListUseCase,
    private val headWorkshopTaskListUseCase: HeadMechanicWorkshopTaskListUseCase,
    private val workshopTaskByIdUseCase: MechanicWorkshopTaskByIdUseCase,
    private val mechanicSubTaskUseCase: MechanicTaskDetailListUseCase,
    private val inputTaskDetailUseCase: MechanicInputTaskDetailUseCase,
    private val updateTaskDetailUseCase: MechanicUpdateTaskDetailUseCase,
    private val uploadProofUseCase: MechanicTaskDetailUploadProofUseCase,
    private val getListTaskProofUseCase: MechanicGetListTaskProofUseCase,
    private val deleteTaskProofUseCase: MechanicDeleteTaskProofUseCase,
    private val uploadProofBeforeUseCase: MechanicTaskDetailUploadProofBeforeUseCase,
    private val getListTaskProofBeforeUseCase: MechanicGetListTaskProofBeforeUseCase
): ViewModel() {
    private var _taskList = MutableLiveData<ResultWrapper<List<MechanicTaskModel>>>()
    val taskList = _taskList as LiveData<ResultWrapper<List<MechanicTaskModel>>>

    private var _taskListById = MutableLiveData<ResultWrapper<List<MechanicTaskModel>>>()
    val taskListById = _taskListById as LiveData<ResultWrapper<List<MechanicTaskModel>>>

    private var _workshopTaskList = MutableLiveData<ResultWrapper<List<MechanicTaskModel>>>()
    val workshopTaskList = _workshopTaskList as LiveData<ResultWrapper<List<MechanicTaskModel>>>

    private var _workshopTaskListById = MutableLiveData<ResultWrapper<List<MechanicTaskModel>>>()
    val workshopTaskListById = _workshopTaskListById as LiveData<ResultWrapper<List<MechanicTaskModel>>>

    private var _subTaskList = MutableLiveData<ResultWrapper<List<MechanicSubTaskModel>>>()
    val subTaskList = _subTaskList as LiveData<ResultWrapper<List<MechanicSubTaskModel>>>

    private var _inputSubTaskDetail = MutableLiveData<ResultWrapper<String>>()
    val inputSubTaskDetail = _inputSubTaskDetail as LiveData<ResultWrapper<String>>

    private var _updateTaskDetail = MutableLiveData<ResultWrapper<String>>()
    val updateTaskDetail = _updateTaskDetail as LiveData<ResultWrapper<String>>

    private var _uploadTaskProof = MutableLiveData<ResultWrapper<String>>()
    val uploadTaskProof = _uploadTaskProof as LiveData<ResultWrapper<String>>

    private var _uploadTaskProofBefore = MutableLiveData<ResultWrapper<String>>()
    val uploadTaskProofBefore = _uploadTaskProofBefore as LiveData<ResultWrapper<String>>

    private var _listTaskProof = MutableLiveData<ResultWrapper<List<MechanicTaskProofModel>>>()
    val listTaskProof = _listTaskProof as LiveData<ResultWrapper<List<MechanicTaskProofModel>>>

    private var _listTaskProofBefore = MutableLiveData<ResultWrapper<List<MechanicTaskProofModel>>>()
    val listTaskProofBefore = _listTaskProofBefore as LiveData<ResultWrapper<List<MechanicTaskProofModel>>>

    private var _deleteTaskProof = MutableLiveData<ResultWrapper<String>>()
    val deleteTaskProof = _deleteTaskProof as LiveData<ResultWrapper<String>>

    fun getTaskList(id: Int) {
        viewModelScope.launch {
            _taskList.value = ResultWrapper.loading()
            mechanicTaskUseCase.addParams(id).execute().run(_taskList::postValue)
        }
    }

    fun getTaskListById(idMechanic: Int, idTask: Int) {
        viewModelScope.launch {
            _taskListById.value = ResultWrapper.loading()
            mechanicTaskByIdUseCase.addParams(idMechanic, idTask).execute().run(_taskListById::postValue)
        }
    }

    fun getTaskList() {
        viewModelScope.launch {
            _taskList.value = ResultWrapper.loading()
            headMechanicTaskUseCase.execute().run(_taskList::postValue)
        }
    }

    fun getWorkshopTaskList(id: Int) {
        viewModelScope.launch {
            _workshopTaskList.value = ResultWrapper.loading()
            workshopTaskListUseCase.addParams(id).execute().run(_workshopTaskList::postValue)
        }
    }

    fun getWorkshopTaskListById(idMechanic: Int, idTask: Int) {
        viewModelScope.launch {
            _workshopTaskListById.value = ResultWrapper.loading()
            workshopTaskByIdUseCase.addParams(idMechanic, idTask).execute().run(_workshopTaskListById::postValue)
        }
    }

    fun getWorkshopTaskList() {
        viewModelScope.launch {
            _workshopTaskList.value = ResultWrapper.loading()
            headWorkshopTaskListUseCase.execute().run(_workshopTaskList::postValue)
        }
    }

    fun getSubTaskList(id: Int) {
        viewModelScope.launch {
            _subTaskList.value = ResultWrapper.loading()
            mechanicSubTaskUseCase.addParams(id).execute().run(_subTaskList::postValue)
        }
    }

    fun inputSubTaskDetail(id: Int, name: String, desc: String, cost: Long, duration: Int) {
        viewModelScope.launch {
            _inputSubTaskDetail.value = ResultWrapper.loading()
            inputTaskDetailUseCase.addParams(id, name, desc, cost, duration).execute().run(_inputSubTaskDetail::postValue)
        }
    }

    fun updateTaskDetail(id: Int, idParentTask: Int, name: String, desc: String, cost: Long, duration: Int) {
        viewModelScope.launch {
            _updateTaskDetail.value = ResultWrapper.loading()
            updateTaskDetailUseCase.addParams(id, idParentTask, name, desc, cost, duration).execute().run(_updateTaskDetail::postValue)
        }
    }

    fun uploadTaskProof(filePath: String, id: Int, name: String, desc: String) {
        viewModelScope.launch {
            _uploadTaskProof.value = ResultWrapper.loading()
            uploadProofUseCase.addParams(filePath, id, name, desc).execute().run(_uploadTaskProof::postValue)
        }
    }

    fun uploadTaskProofBefore(filePath: String, id: Int, name: String, desc: String) {
        viewModelScope.launch {
            _uploadTaskProofBefore.value = ResultWrapper.loading()
            uploadProofBeforeUseCase.addParams(filePath, id, name, desc).execute().run(_uploadTaskProofBefore::postValue)
        }
    }

    fun getListTaskProof(id: Int) {
        viewModelScope.launch {
            _listTaskProof.value = ResultWrapper.loading()
            getListTaskProofUseCase.addParams(id).execute().run(_listTaskProof::postValue)
        }
    }

    fun getListTaskProofBefore(id: Int) {
        viewModelScope.launch {
            _listTaskProofBefore.value = ResultWrapper.loading()
            getListTaskProofBeforeUseCase.addParams(id).execute().run(_listTaskProofBefore::postValue)
        }
    }

    fun deleteTaskProof(id: Int) {
        viewModelScope.launch {
            _deleteTaskProof.value = ResultWrapper.loading()
            deleteTaskProofUseCase.addParams(id).execute().run(_deleteTaskProof::postValue)
        }
    }
}