package com.automa.ui.mechanic.home

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
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.datastore.pref.Preferences
import com.automa.datastore.pref.PreferencesConstant
import com.automa.datastore.user_data.NotificationModel
import com.automa.datastore.user_data.NotificationType
import com.automa.datastore.user_data.UserDataModel
import com.automa.datastore.user_data.UserRoleType
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.ui.R
import com.automa.ui.databinding.FragmentHomeMechanicBinding
import com.automa.ui.mechanic.home.adapter.HomeMechanicTaskAdapter
import com.automa.ui.mechanic.task.MechanicTaskActivity
import com.automa.ui.mechanic.task.MechanicTaskViewModel
import com.automa.ui.shared.deep_link.DeepLinkActivity
import com.automa.ui.shared.scanner.ScannerActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.isNotDefaultData
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HomeMechanicFragment : Fragment() {
    private var _binding: FragmentHomeMechanicBinding ?= null
    private val binding get() = _binding!!

    @Inject lateinit var userData: DataStore<UserDataModel>
    @Inject lateinit var pref: Preferences

    private var taskIdForPendingDeeplink = -1
    private var notifTypeForPendingDeeplink = NotificationType.MECHANIC_TASK_START
    private var selectedMechanicTask: MechanicTaskModel ?= null
    private val CAMERA_PERMISSION = Manifest.permission.CAMERA

    private val taskViewModel: MechanicTaskViewModel by viewModels()
    private val mechanicTaskAdapter = HomeMechanicTaskAdapter(onClick = {
        DialogUtils.showMechanicAcceptTaskDialog(requireContext(), it, onAccept = {
            selectedMechanicTask = it
            if (it.idFleet.isNotDefaultData()) {
                checkPermissions()
            } else {
                startActivity(MechanicTaskActivity.newIntent(requireContext(), it))
            }
        })
    }, onEmptyData = {
        binding.rvMechanicTask.isGone = it
        binding.tvEmptyDo.isVisible = it
    })

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            selectedMechanicTask?.let {
                startScannerActivityForResult.launch(ScannerActivity.newIntentForCheckingFleetMechanic(requireContext(), it))
            }
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
            selectedMechanicTask?.let {
                startActivity(MechanicTaskActivity.newIntent(requireContext(), it))
            }
        } else {
//            selectedMechanicTask?.let {
//                startActivity(MechanicTaskActivity.newIntent(requireContext(), it))
//            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMechanicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initObserver()
        initActions()
        binding.swipeRefreshHomeMechanic.setOnRefreshListener {
            getTaskList()
        }
        getTaskList()
        processPendingDeepLink()
    }

    private fun initActions() {
        binding.rgCurrentTaskType.setOnCheckedChangeListener { _, i ->
            when(i) {
                binding.rbTodayTask.id -> {
                    mechanicTaskAdapter.setFilterToday(true)
                }
                binding.rbAllTask.id -> {
                    mechanicTaskAdapter.setFilterToday(false)
                }
            }
        }
    }

    private fun getTaskList() {
        runBlocking {
            userData.data.first().let {
                if (it.roleType == UserRoleType.MECHANIC) {
                    it.mechanicProfile?.let { profile ->
                        taskViewModel.getTaskList(profile.id)
                    }
                }
                if (it.roleType == UserRoleType.HEAD_MECHANIC) {
                    taskViewModel.getTaskList()
                }
            }
        }
    }

    private fun initRv() {
        binding.rvMechanicTask.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = mechanicTaskAdapter
        }
        mechanicTaskAdapter.setFilterToday(binding.rbTodayTask.isChecked)
        runBlocking {
            userData.data.first().loginData?.let {
                mechanicTaskAdapter.setWorkshopName(it.companyName)
            }
        }
    }

    private fun initObserver() {
        taskViewModel.taskList.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    mechanicTaskAdapter.setData(success.data)
//                    mechanicTaskAdapter.setData(success.data.filter { item -> item.progressStatus != 9 && item.progressStatus != 0 })

//                    taskViewModel.getWorkshopTaskList(20)
                    lifecycleScope.launch {
                        userData.data.first().let { user ->
                            if (user.roleType == UserRoleType.MECHANIC) {
                                user.mechanicProfile?.let { profile ->
                                    taskViewModel.getWorkshopTaskList(profile.id)
                                }
                            }
                            if (user.roleType == UserRoleType.HEAD_MECHANIC) {
                                taskViewModel.getWorkshopTaskList()
                            }
                        }
//                        userData.data.first().mechanicData?.let { md ->
//                            taskViewModel.getWorkshopTaskList(md.profile.id)
//                        }
                    }
                    binding.swipeRefreshHomeMechanic.isRefreshing = false
                }, onFailure = { error ->
                    binding.swipeRefreshHomeMechanic.isRefreshing = false
                    toast(requireContext(), error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        taskViewModel.workshopTaskList.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    mechanicTaskAdapter.addData(success.data)
                    binding.swipeRefreshHomeMechanic.isRefreshing = false
                }, onFailure = { error ->
                    toast(requireContext(), error.errorData.message ?: "Terjadi Kesalahan")
                    binding.swipeRefreshHomeMechanic.isRefreshing = false
                }
            )
        }

        taskViewModel.taskListById.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    success.data.firstOrNull()?.let { task ->
                        lifecycleScope.launch {
                            userData.updateData { d ->
                                val currentList = d.listNotification.toMutableList()
                                val fleetNumber = if (task.fleetRegNumber.isNotDefaultData()) task.fleetRegNumber else task.fleetNotes
                                val addNotification = NotificationModel(notifTypeForPendingDeeplink, listOf(task.maintenanceNumber, fleetNumber))
                                currentList.add(addNotification)
                                d.copy(
                                    listNotification = currentList
                                )
                            }
                        }
                        DialogUtils.showMechanicAcceptTaskDialog(requireContext(), task, onAccept = {
                            selectedMechanicTask = task
                            if (task.idFleet.isNotDefaultData()) {
                                checkPermissions()
                            } else {
                                startActivity(MechanicTaskActivity.newIntent(requireContext(), task))
                            }
                        })
                    } ?: run {
                        lifecycleScope.launch {
                            userData.data.first().let { user ->
                                user.mechanicProfile?.let { profile ->
                                    taskViewModel.getWorkshopTaskListById(profile.id, taskIdForPendingDeeplink)
                                }
                            }
                        }                    }
                }, onFailure = { error ->

                }
            )
        }

        taskViewModel.workshopTaskListById.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    success.data.firstOrNull()?.let { task ->
                        lifecycleScope.launch {
                            userData.updateData { d ->
                                val currentList = d.listNotification.toMutableList()
                                val fleetNumber = if (task.fleetRegNumber.isNotDefaultData()) task.fleetRegNumber else task.fleetNotes
                                val addNotification = NotificationModel(notifTypeForPendingDeeplink, listOf(task.maintenanceNumber, fleetNumber))
                                currentList.add(addNotification)
                                d.copy(
                                    listNotification = currentList
                                )
                            }
                        }
                        DialogUtils.showMechanicAcceptTaskDialog(requireContext(), task, onAccept = {
                            selectedMechanicTask = task
                            if (task.idFleet.isNotDefaultData()) {
                                checkPermissions()
                            } else {
                                startActivity(MechanicTaskActivity.newIntent(requireContext(), task))
                            }
                        })
                    }
                }, onFailure = { error ->

                }
            )
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            selectedMechanicTask?.let {
                startScannerActivityForResult.launch(ScannerActivity.newIntentForCheckingFleetMechanic(requireContext(), it)) }
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

    private fun processPendingDeepLink() {
        val pending = pref.getString(PreferencesConstant.PENDING_DEEPLINK_TYPE)
        val pendingValue = pref.getString(PreferencesConstant.PENDING_DEEPLINK_VALUE)

        when (pending) {
            DeepLinkActivity.TYPE_NOTIF -> {
                pref.saveString(PreferencesConstant.PENDING_DEEPLINK_TYPE, "")
                pref.saveString(PreferencesConstant.PENDING_DEEPLINK_VALUE, "")
                if (pendingValue.contains("/notif/maint/start")) {
                    try {
                        val idTask = pendingValue.split("/").last().toInt()
                        fetchTaskById(idTask, NotificationType.MECHANIC_TASK_START)
                    } catch (e: Exception) {

                    }
                }
                if (pendingValue.contains("/notif/maint/detail")) {
                    try {
                        val idTask = pendingValue.split("/").last().toInt()
                       fetchTaskById(idTask, NotificationType.MECHANIC_TASK_DETAIL)
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    private fun fetchTaskById(id: Int, taskType: NotificationType) {
        taskIdForPendingDeeplink = id
        notifTypeForPendingDeeplink = taskType
        lifecycleScope.launch {
            userData.data.first().let { user ->
                user.mechanicProfile?.let { profile ->
                    taskViewModel.getTaskListById(profile.id, taskIdForPendingDeeplink)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}