package com.automa.ui.driver.check_sheet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.check_sheet.model.CheckSheetDetailModel
import com.automa.domain.check_sheet.model.CheckSheetModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.custom_component.check_sheet.CheckSheet
import com.automa.ui.databinding.ActivityCheckSheetBinding
import com.automa.ui.driver.task.TaskDetailActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class CheckSheetActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CheckSheetActivity::class.java)
        }
    }

    private lateinit var binding: ActivityCheckSheetBinding

    private val checkSheetViewModel: CheckSheetViewModel by viewModels()
    @Inject lateinit var deliveryOrderData: DataStore<UserDataModel>
    private val listCheckSheet = mutableListOf<CheckSheetModel>()
    private val listCheckSheetDetail = mutableListOf<CheckSheetDetailModel>()

    init {
        lifecycleScope.launchWhenCreated {
            deliveryOrderData.data.collect { userData ->
                userData.currentDeliveryOrder?.let { data ->
                    binding.tvDONumber.text = data.deliveryOrderNumber
                    binding.tvDODesc.text = data.deliveryOrderDesc
                    binding.tvDriverAssistant.text = data.driverAssistantName
                    binding.tvFleetPlateNumber.text = data.fleetPlate
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        runBlocking {
            val idDo = deliveryOrderData.data.first().currentDeliveryOrder?.id ?: -1
            checkSheetViewModel.getCheckSheet(idDo)
        }

        initActions()
    }

    private fun initActions() {
        binding.btnNext.setOnClickListener {
            if (checkValid()) {
                val data = getCheckSheetData()
                checkSheetViewModel.updateCheckSheetItem(data)
            } else {
                toast(this, "Harap lengkapi ceklist atau lengkapi laporan")
            }
        }
        binding.btnBackPrevious.setOnClickListener {
            finish()
        }
        binding.toolbarCheckSheet.setOnNavigationClickListener { finish() }
    }

    private fun initObserver() {
        checkSheetViewModel.checkSheet.observe(this) { result ->
            result.handleResult(
                onSuccess = {
                    if (it.data.isEmpty()) {
                        skipCheckSheet()
                    } else {
                        try {
                            if (it.data.first().isApprove == 1) {
                                skipCheckSheet()
                            }
                        } catch (e: Exception) {  }
                        listCheckSheet.addAll(it.data)
                        listCheckSheet.forEach { cs ->
                            checkSheetViewModel.getCheckSheetDetail(cs.id)
                        }
                        binding.btnNext.isEnabled = true
                    }
                }, onFailure = {
                }
            )
        }

        checkSheetViewModel.checkSheetDetail.observe(this) { result->
            result.handleResult(
                onSuccess = {
                    if (it.data.isEmpty()) {
                        skipCheckSheet()
                    } else {
                        listCheckSheetDetail.addAll(it.data)
                        initCheckSheet(it.data)
                        binding.btnNext.isEnabled = true
                    }
                }, onFailure = {
                }
            )
        }

        checkSheetViewModel.updateCheckSheetItem.observe(this) { result ->
            result.handleResult(
                onSuccess = {
                    if (it.data.second.isEmpty()) {
                        runBlocking {
                            deliveryOrderData.updateData { orderData ->
                                orderData.copy(
                                    currentDeliveryOrder = orderData.currentDeliveryOrder?.copy(
                                        checkSheetDone = true
                                    )
                                )
                            }
                        }
                        skipCheckSheet()

//                        DialogUtils.showDialogInfoWithImage(
//                            this@CheckSheetActivity, R.drawable.illustration_document,
//                        getString(R.string.title_waiting_checksheet), getString(R.string.message_waiting_checksheet),
//                        Pair(getString(R.string.button_back_to_home_long)) {
////                            skipCheckSheet()
//                            finish()
//                        })
                    } else {
                        toast(this@CheckSheetActivity, "Gagal mengirim data, silahkan coba lagi")
                    }
                }, onFailure = {

                }
            )
        }
    }

    private fun skipCheckSheet() {
        startActivity(Intent(TaskDetailActivity.newIntent(this@CheckSheetActivity)))
        finish()
    }

    private fun initCheckSheet(data: List<CheckSheetDetailModel>) {
        val view = CheckSheet(this)
        view.setData(data)
        binding.llCheckSheet.addView(view)
    }

    private fun getCheckSheetData(): List<Triple<Int, Int, String>> {
        val data = mutableListOf<Triple<Int, Int, String>>()
        binding.llCheckSheet.children.forEach {
            if (it is CheckSheet) {
                data.addAll(it.getData())
            }
        }
        return data.toList()
    }

    private fun checkValid(): Boolean {
        val data = mutableListOf<Boolean>()
        binding.llCheckSheet.children.forEach {
            if (it is CheckSheet) {
                data.add(it.checkIsValid())
            }
        }
        return data.contains(false).not()
    }
}