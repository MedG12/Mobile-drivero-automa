package com.automa.ui.driver.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.datastore.user_data.NotificationModel
import com.automa.datastore.user_data.NotificationType
import com.automa.datastore.user_data.UserDataModel
import com.automa.ui.R
import com.automa.ui.databinding.FragmentNotifBinding
import com.automa.ui.driver.home.adapter.NotifAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotifFragment : Fragment() {
    private var _binding : FragmentNotifBinding ?= null
    private val binding get() = _binding!!

    private lateinit var notifAdapter: NotifAdapter

    @Inject lateinit var userData: DataStore<UserDataModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotifBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRv()
        initActions()
        lifecycleScope.launch {
            userData.data.collect {
                parseNotification(it.listNotification.asReversed())
            }
        }
    }

    private fun initActions() {
        binding.toolbarNotif.setOnNavigationClickListener {
            if (requireActivity() is HomeActivity) {
                (requireActivity() as HomeActivity).backToHome()
            }
        }
    }

    private fun initRv() {
        notifAdapter = NotifAdapter()
        binding.rvNotif.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = notifAdapter
        }
    }

    private fun parseNotification(data: List<NotificationModel>) {
        binding.tvNotifEmpty.isVisible = data.isEmpty()
        val notifications = mutableListOf<String>()
        data.forEach {
            if (it.fields.count()>=2) {
                val string = when (it.type) {
                    NotificationType.DRIVER_TASK -> getString(R.string.text_notification_driver_task, it.fields[0], it.fields[1])
                    NotificationType.MECHANIC_TASK_START -> getString(R.string.text_notification_mechanic_start, it.fields[0], it.fields[1])
                    NotificationType.MECHANIC_TASK_DETAIL -> getString(R.string.text_notification_mechanic_detail, it.fields[0], it.fields[1])
                }
                notifications.add(string)
            }
        }
        notifAdapter.setData(notifications)
    }
}