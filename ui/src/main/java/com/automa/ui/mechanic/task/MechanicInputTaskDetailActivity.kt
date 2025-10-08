package com.automa.ui.mechanic.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.mechanic_task.model.MechanicSubTaskModel
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityMechanicInputSubTaskDetailBinding
import com.automa.ui.utils.checkIsNotEmpty
import com.automa.ui.utils.onTextValidation
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MechanicInputTaskDetailActivity : BaseActivity() {
    companion object {
        private const val SUB_TASK_DATA = "DATA"
        private const val TASK_ID = "ID"
        private const val IS_NEW_INPUT = "IS_NEW"
        private const val TASK_NAME = "NAME"
        fun newIntentInput(context: Context, id: Int, taskName: String): Intent {
            return Intent(context, MechanicInputTaskDetailActivity::class.java).apply {
                putExtra(TASK_ID, id)
                putExtra(IS_NEW_INPUT, true)
                putExtra(TASK_NAME, taskName)
            }
        }

        fun newIntentEdit(context: Context, id: Int, data: MechanicSubTaskModel, taskName: String): Intent {
            return Intent(context, MechanicInputTaskDetailActivity::class.java).apply {
                putExtra(TASK_ID, id)
                putExtra(SUB_TASK_DATA, data)
                putExtra(TASK_NAME, taskName)
            }
        }
    }

    private lateinit var binding: ActivityMechanicInputSubTaskDetailBinding

    private var subTaskData: MechanicSubTaskModel ?= null
    private var mechanicTaskId = -1
    private var isNewInput = false
    private var taskName = ""

    private val taskViewModel: MechanicTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMechanicInputSubTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isNewInput = intent.getBooleanExtra(IS_NEW_INPUT, false)
        subTaskData = intent.getParcelableExtra(SUB_TASK_DATA)
        mechanicTaskId = intent.getIntExtra(TASK_ID, -1)
        taskName = intent.getStringExtra(TASK_NAME) ?: ""

        initObserver()
        setupValidation()
        initAction()
        binding.tvTaskName.text = String.format("${if (isNewInput) "New" else "Edit"} $taskName Detail")
        if (!isNewInput) setDetailData()
    }

    private fun initAction() {
        binding.btnBack.setOnClickListener { finish() }
        binding.toolbarInputSubTaskDetail.setOnNavigationClickListener { finish() }
        binding.btnSubmit.setOnClickListener {
            binding.tilName.checkIsNotEmpty(binding.edtName)
            binding.tilDescription.checkIsNotEmpty(binding.edtDescription)
            binding.tilCost.checkIsNotEmpty(binding.edtCost)
            binding.tilDuration.checkIsNotEmpty(binding.edtDuration)
            when {
                binding.tilName.error != null -> binding.edtName.requestFocus()
                binding.tilDescription.error != null -> binding.edtDescription.requestFocus()
                binding.tilCost.error != null -> binding.edtCost.requestFocus()
                binding.tilDuration.error != null -> binding.edtDuration.requestFocus()
                else -> {
                    if (isNewInput) {
                        taskViewModel.inputSubTaskDetail(
                            id = mechanicTaskId,
                            name = binding.edtName.text.toString(),
                            desc = binding.edtDescription.text.toString(),
                            cost = binding.edtCost.text.toString().toLong(),
                            duration = binding.edtDuration.text.toString().toInt()
                        )
                    } else {
                        taskViewModel.updateTaskDetail(
                            id = subTaskData?.id ?: -1,
                            idParentTask = mechanicTaskId,
                            name = binding.edtName.text.toString(),
                            desc = binding.edtDescription.text.toString(),
                            cost = binding.edtCost.text.toString().toLong(),
                            duration = binding.edtDuration.text.toString().toInt()
                        )
                    }
                }
            }
        }
    }

    private fun setupValidation() {
        binding.tilName.onTextValidation(binding.edtName)
        binding.tilDescription.onTextValidation(binding.edtDescription)
        binding.tilCost.onTextValidation(binding.edtCost)
        binding.tilDuration.onTextValidation(binding.edtDuration)
    }

    private fun setDetailData() {
        subTaskData?.let {
            binding.edtName.setText(it.taskName)
            binding.edtDescription.setText(it.desc)
            binding.edtCost.setText(it.cost.toString())
            binding.edtDuration.setText(it.duration)
        }
    }

    private fun initObserver() {
        taskViewModel.inputSubTaskDetail.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    finish()
                }, onFailure = { error ->
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        taskViewModel.updateTaskDetail.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    finish()
                }, onFailure = { error ->
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }
    }
}