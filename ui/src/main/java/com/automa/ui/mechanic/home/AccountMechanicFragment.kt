package com.automa.ui.mechanic.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.ui.R
import com.automa.ui.databinding.FragmentAccountMechanicBinding
import com.automa.ui.mechanic.account.AccountEditMechanicActivity
import com.automa.ui.mechanic.breakdown_report.BreakdownReportActivity
import com.automa.ui.mechanic.breakdown_report.HistoryBreakdownReportActivity
import com.automa.ui.shared.auth.LoginActivity
import com.automa.ui.shared.setting.SettingsActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.isNotDefaultData
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AccountMechanicFragment : Fragment() {
    private var _binding: FragmentAccountMechanicBinding ?= null
    private val binding get() = _binding!!

    @Inject lateinit var userData: DataStore<UserDataModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountMechanicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        lifecycleScope.launch {
            userData.data.collect {
                it.mechanicProfile?.let { profile ->
                    Glide.with(requireContext())
                        .load(profile.photoLink)
                        .error(R.drawable.ic_user_default)
                        .into(binding.ivPhoto)
                    val firstName = profile.firstName.lowercase().capitalize(Locale.ROOT)
                    val lastName = profile.lastName.lowercase().capitalize(Locale.ROOT)
                    binding.tvAccountName.text = String.format("$firstName $lastName")
                }
            }
        }

        binding.llLogout.setOnClickListener {
            DialogUtils.showWhiteAlertDialog(requireContext(), getString(R.string.title_logout),
                positiveButton = Pair(getString(R.string.ok)) {
                    runBlocking {
                        userData.updateData {
                            UserDataModel()
                        }
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                }, negativeButton = Pair(getString(R.string.cancel)) {

                }
            )
        }

        binding.btnEditAccount.setOnClickListener {
            startActivity(Intent(requireContext(), AccountEditMechanicActivity::class.java))
        }

        binding.llBreakdownReport.setOnClickListener {
            DialogUtils.showBreakdownReportBottomSheet(
                requireActivity(),
                onAddNew = {
                    startActivity(Intent(requireContext(), BreakdownReportActivity::class.java))
                }, onViewHistory = {
                    startActivity(Intent(requireContext(), HistoryBreakdownReportActivity::class.java))
                }, onViewStatus = {

                }
            )
        }
        binding.llLaporanKerusakan.isGone = true
        binding.llBreakdownReport.isGone = true

        binding.llSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}