package com.automa.ui.mechanic.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityAccountEditMechanicBinding
import com.automa.ui.mechanic.account.certificate.CertificateMechanicActivity
import com.automa.ui.mechanic.account.certificate.InputCertificateMechanicActivity
import com.automa.ui.mechanic.account.certificate.adapter.CertificateListMechanicAdapter
import com.automa.ui.mechanic.account.certificate.adapter.CertificateMechanicAdapter
import com.automa.ui.utils.toast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AccountEditMechanicActivity : BaseActivity() {
    private lateinit var binding: ActivityAccountEditMechanicBinding

    private val accountMechanicViewModel: AccountMechanicViewModel by viewModels()
    @Inject lateinit var userData: DataStore<UserDataModel>

    private val certificateMechanicAdapter = CertificateListMechanicAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountEditMechanicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initRv()
        initActions()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            userData.data.first().mechanicProfile?.let { profile ->
                accountMechanicViewModel.getCertifications(profile.id)
            }
        }
    }

    private fun initActions() {
        binding.btnEditCertificate.setOnClickListener {
            startActivity(Intent(this, CertificateMechanicActivity::class.java))
        }
    }

    private fun initUi() {
        lifecycleScope.launch {
            userData.data.collect {
                it.mechanicProfile?.let { profile ->
                    Glide.with(this@AccountEditMechanicActivity)
                        .load(profile.photoLink)
                        .error(R.drawable.ic_user_default)
                        .into(binding.ivPhoto)
                    binding.tvUsername.text = String.format("${profile.firstName} ${profile.lastName}")
                    binding.tvPhoneNumber.text = profile.phoneNumber
                    binding.tvDob.text = profile.birthDate
                    binding.tvIdNumber.text = profile.nik
                }
            }
        }
    }

    private fun initRv() {
        binding.rvCertificateListMechanic.apply {
            layoutManager = LinearLayoutManager(this@AccountEditMechanicActivity, LinearLayoutManager.VERTICAL, false)
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
    }
}