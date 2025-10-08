package com.automa.ui.mechanic.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.datastore.user_data.UserDataModel
import com.automa.datastore.user_data.UserRoleType
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityMechanicTaskDetailBinding
import com.automa.ui.mechanic.task.adapter.MechanicTaskAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MechanicTaskActivity : BaseActivity() {
    companion object {
        private const val SM_DATA = "DATA"
        fun newIntent(context: Context, data: MechanicTaskModel): Intent {
            return Intent(context, MechanicTaskActivity::class.java).apply {
                putExtra(SM_DATA, data)
            }
        }
    }

    private lateinit var binding: ActivityMechanicTaskDetailBinding
    private lateinit var taskData: MechanicTaskModel
    @Inject lateinit var userData: DataStore<UserDataModel>

    private val taskDetailAdapter: MechanicTaskAdapter = MechanicTaskAdapter(onClick = {
        startActivity(MechanicTaskDetailActivity.newIntent(this, it, it.id, taskData.progressStatus))
    }, onUploadProof = {
        startActivity(MechanicTaskDetailUploadProofActivity.newIntent(this, it, taskData.progressStatus))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMechanicTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMechanicTaskDetail.setOnNavigationClickListener { finish() }

        taskData = intent.getParcelableExtra(SM_DATA)!!
        binding.tvTitleTaskDetail.text = "Tasks of ${taskData.maintenanceTitle}"
        initRv()
        setRvData(taskData)
    }

    private fun initRv() {
        binding.rvMechanicTaskDetail.apply {
            layoutManager = LinearLayoutManager(this@MechanicTaskActivity, LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = taskDetailAdapter
        }

        runBlocking {
            val isHead = userData.data.first().roleType == UserRoleType.HEAD_MECHANIC
            userData.data.first().loginData?.let {
                taskDetailAdapter.setWorkshop(it.companyName)
            }
            taskDetailAdapter.setRoleHeadMechanic(isHead)
        }
    }

    private fun setRvData(data: MechanicTaskModel) {
        taskDetailAdapter.setData(data.tasks)
        taskDetailAdapter.setTaskStatus(data.progressStatus)
    }
}