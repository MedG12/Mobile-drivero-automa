package com.automa.ui.mechanic.account.certificate

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityCertificateMechanicBinding
import com.automa.ui.mechanic.account.AccountMechanicViewModel
import com.automa.ui.mechanic.account.certificate.adapter.CertificateMechanicAdapter
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CertificateMechanicActivity : BaseActivity() {
    private lateinit var binding: ActivityCertificateMechanicBinding
    private val accountMechanicViewModel: AccountMechanicViewModel by viewModels()
    @Inject lateinit var userData: DataStore<UserDataModel>
    private val certificateMechanicAdapter = CertificateMechanicAdapter(onDelete = {
        DialogUtils.showWhiteAlertDialog(this@CertificateMechanicActivity,
            "Hapus Sertifikat",
            "Apakah anda yakin untuk menghapus sertifikat ini?",
            positiveButton = Pair("Ya") {
                accountMechanicViewModel.deleteCertification(it.id)
            },
            negativeButton = Pair("Tidak") {  }
        )
    }, onEdit = {
        startActivity(InputCertificateMechanicActivity.newIntentEdit(this, it))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertificateMechanicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActions()
        initRv()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        getCertificates()
    }

    private fun initActions() {
        binding.toolbarCertificateMechanic.setOnNavigationClickListener { finish() }
        binding.llAddCertificate.setOnClickListener {
            startActivity(Intent(this, InputCertificateMechanicActivity::class.java))
        }
    }

    private fun getCertificates() {
        lifecycleScope.launch {
            userData.data.collect {
                it.mechanicProfile?.let { profile ->
                    accountMechanicViewModel.getCertifications(profile.id)
                }
            }
        }
    }

    private fun initRv() {
        binding.rvCertificateMechanic.apply {
            layoutManager = LinearLayoutManager(this@CertificateMechanicActivity, LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = certificateMechanicAdapter
        }
    }

    private fun initObserver() {
        accountMechanicViewModel.listCertifications.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    certificateMechanicAdapter.setData(success.data)
                }, onFailure = { error ->
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        accountMechanicViewModel.deleteCertification.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    getCertificates()
                }, onFailure = { error ->
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }
    }
}