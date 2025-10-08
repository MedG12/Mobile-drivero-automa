package com.automa.ui.driver.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.ui.R
import com.automa.ui.driver.account.AccountEditActivity
import com.automa.ui.shared.auth.LoginActivity
import com.automa.ui.databinding.FragmentAccountBinding
import com.automa.ui.mechanic.breakdown_report.BreakdownReportActivity
import com.automa.ui.mechanic.breakdown_report.HistoryBreakdownReportActivity
import com.automa.ui.mechanic.service_request.ServiceRequestActivity
import com.automa.ui.shared.scanner.ScannerActivity
import com.automa.ui.shared.setting.SettingsActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.PermissionUtils
import com.automa.ui.utils.toast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding ?= null
    private val binding get() = _binding!!

    @Inject lateinit var userData: DataStore<UserDataModel>

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startScannerActivityForResult.launch(ScannerActivity.newIntentForRefill(requireContext()))
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
            //ACTIONS
            val url = result.data?.getStringExtra(ScannerActivity.RESULT_DATA_QR_LINK_SOLAR) ?: ""
            if (Patterns.WEB_URL.matcher(url).matches()) {
                DialogUtils.showFullScreenImageDialog(requireContext(), url)
            } else {
                toast(requireContext(), "Tidak ada data")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {
        lifecycleScope.launch {
            userData.data.collect {
                it.driverProfile?.let { driverData ->
                    Glide.with(requireContext())
                        .load(driverData.linkImage)
                        .into(binding.ivPhoto)
                    binding.tvAccountName.text = driverData.name.lowercase().capitalize(Locale.ROOT)
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
            startActivity(Intent(requireContext(), AccountEditActivity::class.java))
        }

        binding.llServiceRequest.setOnClickListener {
            DialogUtils.showServiceRequestBottomSheet(
                requireActivity(),
                onAddNew = {
                    startActivity(Intent(requireContext(), ServiceRequestActivity::class.java))
                }, onViewHistory = {

                }, onViewStatus = {

                }
            )
        }

        binding.llSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        binding.llRefill.setOnClickListener {
            checkPermissions()
        }

        binding.llLaporanKerusakan.setOnClickListener {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), PermissionUtils.CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            startScannerActivityForResult.launch(ScannerActivity.newIntentForRefill(requireContext()))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PermissionUtils.CAMERA_PERMISSION)) {
                DialogUtils.showWhiteAlertDialog(
                    requireContext(),
                    getString(R.string.message_permission_needed),
                    getString(R.string.message_request_permission),
                    positiveButton = Pair(getString(R.string.ok)) {
                        requestPermissionLauncher.launch(PermissionUtils.CAMERA_PERMISSION)
                    },
                    negativeButton = Pair(getString(R.string.cancel)) {

                    }
                )
            } else {
                requestPermissionLauncher.launch(PermissionUtils.CAMERA_PERMISSION)
            }
        } else {
            requestPermissionLauncher.launch(PermissionUtils.CAMERA_PERMISSION)
        }
    }
}