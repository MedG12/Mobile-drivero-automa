package com.automa.ui.driver.task.pod

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.driver_task.model.ImagePodModel
import com.automa.domain.driver_task.model.TaskDetailModel
import com.automa.ui.base.BaseActivity
import com.automa.ui.custom_component.AccordionPod
import com.automa.ui.databinding.ActivityViewPodBinding
import com.automa.ui.driver.task.TaskDetailViewModel
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewPodActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context, data: TaskDetailModel): Intent {
            return Intent(context, ViewPodActivity::class.java).apply {
                putExtra(TASK_DETAIL_DATA, data)
            }
        }
        private const val TASK_DETAIL_DATA = "DATA"
    }

    private lateinit var binding: ActivityViewPodBinding

    private val viewModel: TaskDetailViewModel by viewModels()
    @Inject lateinit var deliveryOrderData: DataStore<UserDataModel>

    private lateinit var taskData: TaskDetailModel
    private var idDo = -1
    private var doNumber = ""
    private var fleetPlate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskData = intent.getParcelableExtra(TASK_DETAIL_DATA)!!
        initObserver()

        lifecycleScope.launch {
            deliveryOrderData.data.collect { userData ->
                val data = userData.currentDeliveryOrder
                if (data!=null) {
                    doNumber = data.deliveryOrderNumber
                    fleetPlate = data.fleetPlate
                    idDo = data.id
                    viewModel.getListImage(idDo, taskData.checkIn.map { item -> item.workOrderFrom.id })
                }
            }
        }

        initAction()
    }

    private fun initAction() {
        binding.toolbarViewPod.setOnNavigationClickListener { finish() }
    }

    private fun initObserver() {
        viewModel.listImage.observe(this) {
            binding.llListPod.removeAllViews()
            it.forEachIndexed { index, result ->
                result.handleResult(onSuccess = { success ->
                    addAccordion(success.data, index)
                }, onFailure = { error ->
                    toast(this, error.errorData.toString())
                })
            }
        }
    }

    private fun addAccordion(data: ImagePodModel, index: Int) {
        if (data.result.isNotEmpty()) {
            val view = AccordionPod(this)
            val imageToShow = if (data.result.size>1) data.result.last() else data.result.first()
            val name = try {
                taskData.checkIn[index].workOrderFrom.woNumber
            } catch (e: Exception) {
                doNumber
            }
            view.setData(name, fleetPlate, imageToShow)

            binding.llListPod.addView(view)
        }
    }
}