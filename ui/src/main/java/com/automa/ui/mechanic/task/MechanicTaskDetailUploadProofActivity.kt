package com.automa.ui.mechanic.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.mechanic_task.model.MechanicTaskItemModel
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityMechanicTaskDetailUploadProofBinding
import com.automa.ui.driver.task.pod.UploadPhotoBottomSheetFragment
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MechanicTaskDetailUploadProofActivity : BaseActivity() {
    private lateinit var binding: ActivityMechanicTaskDetailUploadProofBinding
    companion object {
        private const val TASK_DATA = "DATA"
        private const val PROGRESS_STATUS = "STATUS"
        fun newIntent(context: Context, data: MechanicTaskItemModel, progressStatus: Int): Intent {
            return Intent(context, MechanicTaskDetailUploadProofActivity::class.java).apply {
                putExtra(TASK_DATA, data)
                putExtra(PROGRESS_STATUS, progressStatus)
            }
        }
    }

    private val taskViewModel: MechanicTaskViewModel by viewModels()
    private var taskData: MechanicTaskItemModel ?= null
    private var progressStatus = 0
    @Inject lateinit var userData: DataStore<UserDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMechanicTaskDetailUploadProofBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskData = intent.getParcelableExtra(TASK_DATA)
        progressStatus = intent.getIntExtra(PROGRESS_STATUS, 0)
        initUi()

        initActions()
        initObserver()
        taskViewModel.getListTaskProof(taskData?.id ?: -1)
        taskViewModel.getListTaskProofBefore(taskData?.id ?: -1)
    }

    private fun initActions() {
        binding.toolbarMechanicTaskDetailUploadProof.setOnNavigationClickListener { finish() }
        binding.layoutImageAfter.apply {
            setAnchor(binding.clRootUploadProof)
            setMaxPhotos(3)
            setFragmentManager(supportFragmentManager)
            setCanUpload(progressStatus in 5..6)
            setOnDelete {
                taskViewModel.deleteTaskProof(it.id)
            }
            setOnUploadClick {
                UploadPhotoBottomSheetFragment.newInstanceUploadProof(
                    taskData?.id ?: -1,
                    taskData?.taskName ?: "",
                    UploadPhotoBottomSheetFragment.PROOF_AFTER,
                    onFinish = {
                        taskViewModel.getListTaskProof(taskData?.id ?: -1)
                    }).show(supportFragmentManager, "")
            }
        }

        binding.layoutImageBefore.apply {
            setAnchor(binding.clRootUploadProof)
            setMaxPhotos(3)
            setFragmentManager(supportFragmentManager)
            setCanUpload(progressStatus in 5..6)
            setOnDelete {
                taskViewModel.deleteTaskProof(it.id)
            }
            setOnUploadClick {
                UploadPhotoBottomSheetFragment.newInstanceUploadProof(
                    taskData?.id ?: -1,
                    taskData?.taskName ?: "",
                    UploadPhotoBottomSheetFragment.PROOF_BEFORE,
                    onFinish = {
                        taskViewModel.getListTaskProofBefore(taskData?.id ?: -1)
                    }).show(supportFragmentManager, "")
            }
        }
    }

    private fun initUi() {
        runBlocking {
            userData.data.first().loginData?.let {
                binding.tvWorkshop.text = it.companyName
            }
        }
        taskData?.let {
            binding.tvMaintenanceNumber.text = it.taskName
            binding.tvMechanicName.text = String.format("${it.mechanicFirstName} ${it.mechanicLastName}")
            binding.tvPlateFleet.text = it.fleetPlate
            binding.tvScheduledTime.text = it.scheduledDateTime
            binding.tvDuration.text = String.format("${it.totalDuration} Menit")
        }
    }

    private fun initObserver() {
        taskViewModel.listTaskProof.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    binding.layoutImageAfter.setData(success.data)
                }, onFailure = { error ->

                }
            )
        }

        taskViewModel.listTaskProofBefore.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    binding.layoutImageBefore.setData(success.data)
                }, onFailure = { error ->

                }
            )
        }

//        taskViewModel.uploadTaskProof.observe(this) {
//            it.handleResult(
//                onSuccess = { success ->
//                    toast(this, "Berhasil Menambahkan Foto")
//                    taskViewModel.getListTaskProof(taskData?.id ?: -1)
//                }, onFailure = { error ->
//                    toast(this, "Gagal Menambahkan Foto")
//                }
//            )
//        }
//
//        taskViewModel.uploadTaskProofBefore.observe(this) {
//            it.handleResult(
//                onSuccess = { success ->
//                    toast(this, "Berhasil Menambahkan Foto")
//                    taskViewModel.getListTaskProofBefore(taskData?.id ?: -1)
//                }, onFailure = { error ->
//                    toast(this, "Gagal Menambahkan Foto")
//                }
//            )
//        }

        taskViewModel.deleteTaskProof.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    toast(this, "Berhasil Menghapus Foto")
                    taskViewModel.getListTaskProof(taskData?.id ?: -1)
                }, onFailure = { error ->
                    toast(this, "Gagal Menghapus Foto")
                }
            )
        }
    }
}