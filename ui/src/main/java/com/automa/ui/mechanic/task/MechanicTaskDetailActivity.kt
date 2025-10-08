package com.automa.ui.mechanic.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.mechanic_task.model.MechanicTaskItemModel
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityMechanicSubTaskDetailBinding
import com.automa.ui.mechanic.task.adapter.MechanicTaskDetailAdapter
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MechanicTaskDetailActivity : BaseActivity() {
    companion object {
        private const val SM_DATA = "DATA"
        private const val ID_SCHEDULED_MT = "ID"
        private const val PROGRESS_STATUS = "STATUS"

        fun newIntent(context: Context, data: MechanicTaskItemModel, idMt: Int, progressStatus: Int): Intent {
            return Intent(context, MechanicTaskDetailActivity::class.java).apply {
                putExtra(SM_DATA, data)
                putExtra(ID_SCHEDULED_MT, idMt)
                putExtra(PROGRESS_STATUS, progressStatus)
            }
        }
    }

    private lateinit var binding: ActivityMechanicSubTaskDetailBinding

    private val taskViewModel: MechanicTaskViewModel by viewModels()
    private lateinit var taskData: MechanicTaskItemModel
    private var idScheduledMt: Int = -1
    private var progressStatus: Int = -1
    private val subTaskAdapter = MechanicTaskDetailAdapter(onClick = {
    }, onEdit = {
        Log.d("TAG-TASK", "ItemClickTaskDetail: $it, ${taskData.taskName}")
        startActivity(MechanicInputTaskDetailActivity.newIntentEdit(this, taskData.id, it, taskData.taskName))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMechanicSubTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskData = intent.getParcelableExtra(SM_DATA)!!
        idScheduledMt = intent.getIntExtra(ID_SCHEDULED_MT, -1)
        progressStatus = intent.getIntExtra(PROGRESS_STATUS, -1)
        binding.llAddTaskDetail.isVisible = progressStatus == 2 || progressStatus in 5..6

        initObserver()
        initRv()

        binding.tvTitleSubTaskDetail.text = "Tasks Detail of ${taskData.taskName}"
        binding.llAddTaskDetail.setOnClickListener {
            startActivity(MechanicInputTaskDetailActivity.newIntentInput(this, taskData.id, taskData.taskName))
        }
        binding.toolbarMechanicSubTaskDetail.setOnNavigationClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.getSubTaskList(idScheduledMt)
    }

    private fun initRv() {
        binding.rvMechanicTaskDetail.apply {
            layoutManager = LinearLayoutManager(this@MechanicTaskDetailActivity, LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = subTaskAdapter
        }
        subTaskAdapter.setProgressStatus(progressStatus)
    }

    private fun initObserver() {
        taskViewModel.subTaskList.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    subTaskAdapter.setData(success.data)
                }, onFailure = { error ->
                    toast(this, "FAILURE")
                }
            )
        }
    }
}