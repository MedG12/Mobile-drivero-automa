package com.automa.ui.driver.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.core.DataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.datastore.pref.Preferences
import com.automa.datastore.pref.PreferencesConstant
import com.automa.datastore.user_data.CurrentDeliveryOrder
import com.automa.datastore.user_data.NotificationModel
import com.automa.datastore.user_data.NotificationType
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.domain.driver_task.model.MasterDeliveryOrderCategoryModel
import com.automa.domain.driver_task.model.MasterDeliveryOrderSubCategoryModel
import com.automa.ui.R
import com.automa.ui.driver.check_sheet.CheckSheetActivity
import com.automa.ui.databinding.FragmentHomeBinding
import com.automa.ui.driver.check_sheet.CheckSheetViewModel
import com.automa.ui.driver.home.adapter.DeliveryOrderAdapter
import com.automa.ui.driver.home.adapter.FinishedDeliveryOrderAdapter
import com.automa.ui.shared.scanner.ScannerActivity
import com.automa.ui.driver.task.TaskDetailActivity
import com.automa.ui.shared.deep_link.DeepLinkActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.isNotDefaultData
import com.automa.ui.utils.mapToLocal
import com.automa.ui.utils.setItems
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding ?= null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()
    private val checkSheetViewModel: CheckSheetViewModel by viewModels()
    private lateinit var deliveryOrderAdapter: DeliveryOrderAdapter
    private lateinit var finishedDeliveryOrderAdapter: FinishedDeliveryOrderAdapter

    private var selectedDeliverOrder: DeliveryOrderModel ?= null
    @Inject lateinit var userData: DataStore<UserDataModel>
    @Inject lateinit var pref: Preferences

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA

    lateinit var toggleDrawer: ActionBarDrawerToggle
    private var idDriver = -1
    private var idPairedFleet = -1

    private var selectedCategory = -1
    private var selectedSubCategory = -1

    private var isDuplicateMaster = false

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startScannerActivityForResult.launch(ScannerActivity.newIntentForCheckingFleet(requireContext(), selectedDeliverOrder!!))
        } else {
            DialogUtils.showWhiteAlertDialog(
                requireContext(),
                getString(R.string.message_permission_needed),
                getString(R.string.message_request_permission),
                positiveButton = Pair(getString(R.string.ok)) {

                }
            )
        }
    }

    private val startScannerActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            updateCurrentTask(selectedDeliverOrder!!.mapToLocal())
            startActivity(CheckSheetActivity.newIntent(requireContext()))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            userData.data.collect {
                it.driverProfile?.let { profile ->
                    val name = profile.name.lowercase().capitalize(Locale.ROOT)
                    val licenseNumber = profile.licNumber
                    binding.tvUsername.text = String.format("$name $licenseNumber")
                    idDriver = profile.id
                    idPairedFleet = profile.pairedFleet?.id ?:-1
                }
                it.refreshHome?.let { refresh ->
                    if (refresh) {
                        refreshDeliveryOrder()
                        userData.updateData { d ->
                            d.copy(
                                refreshHome = false
                            )
                        }
                    }
                }
            }
        }
        initObserver()
        initRv()
        binding.edtUsername.doAfterTextChanged {
            deliveryOrderAdapter.filter(it.toString())
            finishedDeliveryOrderAdapter.filter(it.toString())
        }
//        initUi()
        initActions()
        //hide finished task
        binding.llFinishedTasks.isVisible = false
        processPendingDeepLink()
    }

    private fun initUi() {
        toggleDrawer = ActionBarDrawerToggle(requireActivity(), binding.drawerHome, R.string.title_activity_drawer, R.string.title_activity_drawer)
        binding.drawerHome.addDrawerListener(toggleDrawer)
        toggleDrawer.syncState()
    }

    private fun initActions() {
//        binding.toolbarMenuHome.setOnShowMenuClickListener {
//            binding.drawerHome.openDrawer(GravityCompat.START)
//            binding.navView.bringToFront()
//        }
        binding.swipeRefreshHomeDriver.setOnRefreshListener {
            refreshDeliveryOrder()
        }

        binding.rgCurrentTaskType.setOnCheckedChangeListener { _, i ->
            refreshDeliveryOrder()
        }
        binding.rbTodayTask.isChecked = true
        viewModel.getDeliveryOrder(0, idDriver, idPairedFleet, 0)
        viewModel.getMasterDeliveryOrderCategory()
    }

    private fun refreshDeliveryOrder() {
        binding.llCategoryMaster.isVisible = false
        when (binding.rgCurrentTaskType.checkedRadioButtonId) {
            binding.rbTodayTask.id -> {
                deliveryOrderAdapter.setTaskType(DeliveryOrderAdapter.TYPE_DELIVERY_ORDER)
                deliveryOrderAdapter.resetExpandedPosition()
                viewModel.getDeliveryOrder(0, idDriver, idPairedFleet, 0)
            }
            binding.rbAllTask.id -> {
                deliveryOrderAdapter.setTaskType(DeliveryOrderAdapter.TYPE_DELIVERY_ORDER)
                deliveryOrderAdapter.resetExpandedPosition()
                val show = pref.getInt(PreferencesConstant.SHOW_DATA, PreferencesConstant.DEFAULT_SHOW_DATA)
                viewModel.getDeliveryOrder(show, idDriver, idPairedFleet, 0)
            }
            binding.rbMasterData.id -> {
                deliveryOrderAdapter.setTaskType(DeliveryOrderAdapter.TYPE_MASTER_DATA_DO)
                deliveryOrderAdapter.resetExpandedPosition()
                binding.llCategoryMaster.isVisible = true
                binding.tvEmptyDo.isVisible = false
                if (selectedCategory.isNotDefaultData() && selectedSubCategory.isNotDefaultData()) {
                    viewModel.getMasterDeliveryOrder(selectedCategory, selectedSubCategory)
                } else {
                    deliveryOrderAdapter.setData(emptyList())
                }
            }
            binding.rbTaskHistory.id -> {
                deliveryOrderAdapter.setTaskType(DeliveryOrderAdapter.TYPE_DELIVERY_ORDER_DONE)
                deliveryOrderAdapter.resetExpandedPosition()
                val show = pref.getInt(PreferencesConstant.SHOW_DATA, PreferencesConstant.DEFAULT_SHOW_DATA)
                viewModel.getDeliveryOrder(show, idDriver, -1, 1)
            }
        }
    }

    private fun initObserver() {
        viewModel.deliveryOrderList.observe(viewLifecycleOwner) { result ->
            result.handleResult(
                onSuccess = {
                    val filtered = it.data.filter { item ->
                        idDriver == item.idDriver
                    }.asReversed()
                    deliveryOrderAdapter.setData(filtered)
//                    finishedDeliveryOrderAdapter.setData(it.data)
                    binding.swipeRefreshHomeDriver.isRefreshing = false
                    binding.rvDeliveryOrder.isVisible = filtered.isNotEmpty()
                    binding.progressHomeDo.isVisible = false
                    binding.tvEmptyDo.isVisible = filtered.isEmpty()
                }, onFailure = {
                    toast(requireContext(), it.errorData.message ?: "Terjadi Kesalahan")
                    binding.swipeRefreshHomeDriver.isRefreshing = false
                    binding.rvDeliveryOrder.isVisible = true
                    binding.progressHomeDo.isVisible = false
                    binding.tvEmptyDo.isVisible = false
                }, onLoading = {
                    binding.rvDeliveryOrder.isVisible = false
                    binding.progressHomeDo.isVisible = true
                    binding.tvEmptyDo.isVisible = false
                }
            )
        }

        viewModel.workOrder.observe(viewLifecycleOwner) { result ->
            result.handleResult(
                onSuccess = {
                    selectedDeliverOrder?.let { selectedOrder ->
                        DialogUtils.showWorkOrderDialog(requireActivity(), selectedOrder, it.data) {
                            checkPermissions()
//                        startScannerActivityForResult.launch(Intent(requireContext(), ScannerActivity::class.java))
                            //todo remove below, used for testing purposes
//                        updateCurrentTask(selectedDeliverOrder!!.mapToLocal())
//                        startActivity(TaskDetailActivity.newIntent(requireContext()))
                        }
                    }
                }, onFailure = {
                    toast(requireContext(), it.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        viewModel.getDuplicateWorkOrder.observe(viewLifecycleOwner) { result ->
            result.handleResult(
                onSuccess = {
                    DialogUtils.showDuplicateDeliveryOrderDialog(
                        requireActivity(),
                        childFragmentManager,
                        selectedDeliverOrder!!,
                        it.data,
                        isDuplicateMaster
                    ) { idDO, assignedDate, estDepartureTime, endDate ->
                        selectedDeliverOrder?.let { order ->
                            runBlocking {
                                userData.data.first().driverProfile?.let { profile ->
                                    val idFleet = if (isDuplicateMaster) profile.pairedFleet?.id ?: -1 else order.idFleet
                                    viewModel.postDuplicateDeliveryOrder(idDO, assignedDate, estDepartureTime, endDate, idFleet, idDriver)
                                }
                            }
                        }
                    }
                }, onFailure = {
                    toast(requireContext(), it.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        viewModel.postDuplicateDeliveryOrder.observe(viewLifecycleOwner) { result ->
            result.handleResult(
                onSuccess = {
                    if (it.data == 1) {
                        toast(requireContext(), "Berhasil")
                        refreshDeliveryOrder()
                    } else {
                        toast(requireContext(), "Gagal")
                    }
                }, onFailure = {
                    toast(requireContext(), it.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        checkSheetViewModel.checkSheet.observe(viewLifecycleOwner) { result ->
            result.handleResult(
                onSuccess = {
                    if (it.data.isEmpty()) {
                        startActivity(TaskDetailActivity.newIntent(requireContext()))
                    }
                    try {
                        if (it.data.first().isApprove == 1) {
                            startActivity(TaskDetailActivity.newIntent(requireContext()))
                        } else {
                            DialogUtils.showDialogInfoWithImage(
                                requireContext(), R.drawable.illustration_document,
                                getString(R.string.title_waiting_checksheet), getString(R.string.message_waiting_checksheet),
                                Pair(getString(R.string.button_back)) {

                                })
                        }
                    } catch (e: Exception) {  }
                }, onFailure = {
                }
            )
        }

        viewModel.masterDeliveryOrderList.observe(viewLifecycleOwner) { result ->
            result.handleResult(
                onSuccess = {
                    try {
                        deliveryOrderAdapter.setData(it.data.first().subCategory.first().masterDoList)
                        binding.rvDeliveryOrder.isVisible = it.data.first().subCategory.first().masterDoList.isNotEmpty()
                        binding.tvEmptyDo.isVisible = it.data.first().subCategory.first().masterDoList.isEmpty()
                    } catch (e: Exception) {
                        deliveryOrderAdapter.setData(emptyList())
                        binding.rvDeliveryOrder.isVisible = false
                        binding.tvEmptyDo.isVisible = true
                    }
                    binding.swipeRefreshHomeDriver.isRefreshing = false
                    binding.progressHomeDo.isVisible = false
                }, onFailure = {
                    toast(requireContext(), it.errorData.message ?: "Terjadi Kesalahan")
                    binding.swipeRefreshHomeDriver.isRefreshing = false
                    binding.rvDeliveryOrder.isVisible = true
                    binding.progressHomeDo.isVisible = false
                    binding.tvEmptyDo.isVisible = false
                }, onLoading = {
                    binding.rvDeliveryOrder.isVisible = false
                    binding.progressHomeDo.isVisible = true
                    binding.tvEmptyDo.isVisible = false
                }
            )
        }

        viewModel.masterDeliveryOrderCategory.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    binding.atvCategory.setItems(success.data.map { item -> item.name }.toMutableList())
                    binding.atvCategory.setOnItemClickListener { _, _, i, _ ->
                        parseSelectedCategory(i, success.data)
                        viewModel.getMasterDeliveryOrderSubCategory(selectedCategory)
                    }
                    binding.atvCategory.setOnClickListener {
                        binding.atvCategory.showDropDown()
                    }
                }, onFailure = { error ->

                }
            )
        }

        viewModel.masterDeliveryOrderSubCategory.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    deliveryOrderAdapter.setData(emptyList())
                    binding.tvEmptyDo.isVisible = false
                    binding.atvSubCategory.setText("")
                    selectedSubCategory = -1
                    binding.atvSubCategory.setItems(success.data.map { item -> item.name }.toMutableList())
                    binding.atvSubCategory.setOnItemClickListener { _, _, i, _ ->
                        parseSelectedSubCategory(i, success.data)
                        viewModel.getMasterDeliveryOrder(selectedCategory, selectedSubCategory)
                    }
                    binding.atvSubCategory.setOnClickListener {
                        binding.atvSubCategory.showDropDown()
                    }
                }, onFailure = { error ->

                }
            )
        }

        viewModel.deliveryOrderByIdList.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    success.data.firstOrNull()?.let { order ->
                        selectedDeliverOrder = order
                        lifecycleScope.launch {
                            userData.updateData { d ->
                                val currentList = d.listNotification.toMutableList()
                                val addNotification = NotificationModel(NotificationType.DRIVER_TASK, listOf(order.deliveryOrderNumber, order.fleetPlate))
                                currentList.add(addNotification)
                                d.copy(
                                    listNotification = currentList
                                )
                            }
                        }
                        viewModel.getWorkOrder(order.id)
                    }
                }, onFailure = { error ->

                }
            )
        }
    }

    private fun parseSelectedCategory(position: Int, list: List<MasterDeliveryOrderCategoryModel>) {
        return try {
            selectedCategory = list[position].id
        } catch (e: Exception) {
            selectedCategory = -1
        }
    }

    private fun parseSelectedSubCategory(position: Int, list: List<MasterDeliveryOrderSubCategoryModel>) {
        return try {
            selectedSubCategory = list[position].id
        } catch (e: Exception) {
            selectedSubCategory = -1
        }
    }

    private fun initRv() {
        deliveryOrderAdapter = DeliveryOrderAdapter(listener = object : DeliveryOrderAdapter.DeliveryOrderClickListener {
            override fun onItemClicked(data: DeliveryOrderModel, type: Int) {
                if (type == DeliveryOrderAdapter.TYPE_DELIVERY_ORDER) {
                    if (runBlocking { userData.data.first().currentDeliveryOrder?.id == data.id }) {
//                    runBlocking {
//                        val idDo = userData.data.first().currentDeliveryOrder?.id ?: -1
//                        checkSheetViewModel.getCheckSheet(idDo)
//                    }
                        startActivity(TaskDetailActivity.newIntent(requireContext()))
                    } else {
                        selectedDeliverOrder = data
                        viewModel.getWorkOrder(data.id)
                    }
                }
                if (type == DeliveryOrderAdapter.TYPE_DELIVERY_ORDER_DONE) {
                    startActivity(TaskDetailActivity.newIntent(requireContext(), data))
                }
            }

            override fun onLongClicked(data: DeliveryOrderModel) {
                if (runBlocking { userData.data.first().currentDeliveryOrder != null }) {
                    DialogUtils.showWhiteAlertDialog(
                        requireContext(),
                        title = "Batal ambil tugas",
                        message = "Anda yakin untuk membatalkan pengambilan tugas ini?",
                        positiveButton = Pair("Ya") {
                            updateCurrentTask(null)
                        },  negativeButton = Pair("Tidak") {  })
                }
            }

            override fun onDuplicateClicked(data: DeliveryOrderModel, type: Int) {
                if (type == DeliveryOrderAdapter.TYPE_MASTER_DATA_DO) {
                    runBlocking {
                        userData.data.first().driverProfile?.let {
                            if (it.pairedFleet != null) {
                                isDuplicateMaster = true
                                selectedDeliverOrder = data
                                viewModel.getDuplicateWorkOrder(data.id)
                            } else {
                                DialogUtils.showDialogInfoWithImage(
                                    requireContext(), R.drawable.illustration_cancel_pairing,
                                    getString(R.string.title_fail_duplicate_do), getString(R.string.message_fail_duplicate_do),
                                    Pair(getString(R.string.button_back)) {

                                    })
                            }
                        }
                    }
                } else {
                    isDuplicateMaster = false
                    selectedDeliverOrder = data
                    viewModel.getDuplicateWorkOrder(data.id)
                }
            }
        })
        binding.rvDeliveryOrder.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = deliveryOrderAdapter
            itemAnimator = null
        }
        lifecycleScope.launchWhenCreated {
            userData.data.collect {
                deliveryOrderAdapter.setCurrentActiveTask(it.currentDeliveryOrder?.id ?: -1)
                deliveryOrderAdapter.setAllowDuplicateDO(it.driverProfile?.statusCopyDo ?: false)
            }
        }

        finishedDeliveryOrderAdapter = FinishedDeliveryOrderAdapter(listener = object : FinishedDeliveryOrderAdapter.DeliveryOrderClickListener {
            override fun onItemClicked(data: DeliveryOrderModel) {

            }
        })
        binding.rvFinishedDeliveryOrder.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = finishedDeliveryOrderAdapter
        }
    }

    private fun updateCurrentTask(currentTask: CurrentDeliveryOrder?) {
        lifecycleScope.launch {
            userData.updateData {
                it.copy(
                    currentDeliveryOrder = currentTask
                )
            }
        }
    }

    private fun processPendingDeepLink() {
        val pending = pref.getString(PreferencesConstant.PENDING_DEEPLINK_TYPE)
        val pendingValue = pref.getString(PreferencesConstant.PENDING_DEEPLINK_VALUE)
        when (pending) {
            DeepLinkActivity.TYPE_NOTIF -> {
                if (pendingValue.contains("/notif/do/")) {
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_TYPE, "")
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_VALUE, "")
                    try {
                        val idDo = pendingValue.split("/").last().toInt()
                        viewModel.getDeliveryOrderById(idDo)
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            startScannerActivityForResult.launch(ScannerActivity.newIntentForCheckingFleet(requireContext(), selectedDeliverOrder!!))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(CAMERA_PERMISSION)) {
                DialogUtils.showWhiteAlertDialog(
                    requireContext(),
                    getString(R.string.message_permission_needed),
                    getString(R.string.message_request_permission),
                    positiveButton = Pair(getString(R.string.ok)) {
                        requestPermissionLauncher.launch(CAMERA_PERMISSION)
                    },
                    negativeButton = Pair(getString(R.string.cancel)) {

                    }
                )
            } else {
                requestPermissionLauncher.launch(CAMERA_PERMISSION)
            }
        } else {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }
    }
}