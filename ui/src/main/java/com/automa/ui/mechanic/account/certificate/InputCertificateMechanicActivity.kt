package com.automa.ui.mechanic.account.certificate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.account.model.CertificationMechanicModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityInputCertificateMechanicBinding
import com.automa.ui.mechanic.account.AccountMechanicViewModel
import com.automa.ui.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class InputCertificateMechanicActivity : BaseActivity() {
    companion object {
        private const val CERT_DATA = "CERT_DATA"
        fun newIntentEdit(context: Context, data: CertificationMechanicModel): Intent {
            return Intent(context, InputCertificateMechanicActivity::class.java).apply {
                putExtra(CERT_DATA, data)
            }
        }
    }
    private lateinit var binding: ActivityInputCertificateMechanicBinding
    private val accountMechanicViewModel: AccountMechanicViewModel by viewModels()

    private var selectedCertDate = ""
    private var selectedExpDate = ""
    private var selectedLevel = ""
    private var existingData: CertificationMechanicModel ?= null
    @Inject lateinit var userData: DataStore<UserDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputCertificateMechanicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        existingData = intent.getParcelableExtra(CERT_DATA)
        if (existingData != null) setupExistingData()

        initActions()
        setupValidation()
        initObserver()
    }

    private fun setupExistingData() {
        existingData?.let { data ->
            data.name.ifNotDefaultData { binding.edtCertName.setText(it) }
            data.level.ifNotDefaultData {
                selectedLevel = it
                binding.edtLevel.setText(it)
            }
            data.certDate.ifNotDefaultData {
                selectedCertDate = it
                binding.edtCertDate.setText(it)
            }
            data.certExpirationDate.ifNotDefaultData {
                selectedExpDate = it
                binding.edtExpDate.setText(it)
            }
            data.desc.ifNotDefaultData { binding.edtDescription.setText(it) }
        }
    }

    private fun setupValidation() {
        binding.tilCertName.onTextValidation(binding.edtCertName)
        binding.tilCertDate.onTextValidation(binding.edtCertDate)
        binding.tilLevel.onTextValidation(binding.edtLevel)
        binding.tilExpDate.onTextValidation(binding.edtExpDate)
    }

    private fun initActions() {
        binding.toolbarInputCertificateMechanic.setOnNavigationClickListener { finish() }

        val datePickerCertDate = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val datePickerExpDate = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePickerCertDate.addOnPositiveButtonClickListener {
            selectedCertDate = DateUtils.formatDate(Date(it), "yyyy-MM-dd")
            binding.edtCertDate.setText(selectedCertDate)
        }

        datePickerExpDate.addOnPositiveButtonClickListener {
            selectedExpDate = DateUtils.formatDate(Date(it), "yyyy-MM-dd")
            binding.edtExpDate.setText(selectedExpDate)
        }

        binding.edtCertDate.setOnClickListener {
            datePickerCertDate.show(supportFragmentManager, "")
        }

        binding.edtExpDate.setOnClickListener {
            datePickerExpDate.show(supportFragmentManager, "")
        }

        val listLevel = mutableListOf("Beginner", "Intermediate", "Expert")
        binding.edtLevel.setItems(listLevel)
        binding.edtLevel.setOnClickListener {
            binding.edtLevel.showDropDown()
        }
        binding.edtLevel.setOnItemClickListener { _, _, position, _ ->
            selectedLevel = listLevel[position]
        }

        binding.btnSubmit.setOnClickListener {
            binding.tilCertName.checkIsNotEmpty(binding.edtCertName)
            binding.tilCertDate.checkIsNotEmpty(binding.edtCertDate)
            binding.tilLevel.checkIsNotEmpty(binding.edtLevel)
            if (existingData != null) binding.tilExpDate.checkIsNotEmpty(binding.edtExpDate)
            when {
                binding.tilCertName.error != null -> binding.edtCertName.requestFocus()
                binding.tilCertDate.error != null -> binding.edtCertDate.requestFocus()
                binding.tilLevel.error != null -> binding.edtLevel.requestFocus()
                binding.tilExpDate.error != null -> binding.edtExpDate.requestFocus()
                else -> {
                    if (existingData != null) {
                        existingData?.let { existing ->
                            accountMechanicViewModel.updateCertification(
                                id = existing.id,
                                idMechanic = existing.idMechanic,
                                name = binding.edtCertName.text.toString(),
                                certDate = selectedCertDate,
                                expDate = selectedExpDate,
                                level = selectedLevel,
                                desc = binding.edtDescription.text.toString()
                            )
                        }
                    } else {
                        runBlocking {
                            userData.data.first().mechanicProfile?.let { profile ->
                                accountMechanicViewModel.inputCertification(
                                    idMechanic = profile.id,
                                    name = binding.edtCertName.text.toString(),
                                    certDate = selectedCertDate,
                                    expDate = selectedExpDate,
                                    level = selectedLevel,
                                    desc = binding.edtDescription.text.toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initObserver() {
        accountMechanicViewModel.inputCertification.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    toast(this, "Berhasil")
                    finish()
                }, onFailure = { error ->
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        accountMechanicViewModel.updateCertification.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    toast(this, "Berhasil")
                    finish()
                }, onFailure = { error ->
                    toast(this, error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }
    }
}