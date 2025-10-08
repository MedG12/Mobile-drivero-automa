package com.automa.ui.mechanic.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityRegisterMechanicBinding
import com.automa.ui.shared.auth.AuthViewModel
import com.automa.ui.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RegisterMechanicActivity : BaseActivity() {
    @Inject
    lateinit var userData: DataStore<UserDataModel>

    private val authViewModel: AuthViewModel by viewModels()

    private var selectedDate = ""

    private lateinit var binding: ActivityRegisterMechanicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMechanicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initValidation()
        initActions()
        initObserver()
    }

    private fun initValidation() {
        binding.tilNik.onTextValidation(binding.edtNik)
        binding.tilFirstName.onTextValidation(binding.edtFirstName)
        binding.tilLastName.onTextValidation(binding.edtLastName)
        binding.tilDate.onTextValidation(binding.edtDate)
        binding.tilEmail.onTextEmailValidation(binding.edtEmail)
        binding.tilUsername.onTextValidation(binding.edtUsername)
        binding.tilPassword.onTextValidation(binding.edtPassword)
        binding.tilPasswordConfirmation.onTextChangeConfirmPasswordValidation(binding.edtPasswordConfirmation, binding.edtPassword)
        binding.tilWhatsappNumber.onTextValidation(binding.edtWhatsappNumber)
        binding.tilPhoneNumber.onTextValidation(binding.edtPhoneNumber)
    }

    private fun initActions() {
        binding.btnSubmitRegister.setOnClickListener {
            binding.tilNik.checkIsNotEmpty(binding.edtNik)
            binding.tilFirstName.checkIsNotEmpty(binding.edtFirstName)
            binding.tilLastName.checkIsNotEmpty(binding.edtLastName)
            binding.tilDate.checkIsNotEmpty(binding.edtDate)
            binding.tilEmail.checkEmailValidation(binding.edtEmail)
            binding.tilUsername.checkIsNotEmpty(binding.edtUsername)
            binding.tilPassword.checkIsNotEmpty(binding.edtPassword)
            binding.tilPasswordConfirmation.checkConfirmationPassword(binding.edtPasswordConfirmation, binding.edtPassword)
            binding.tilWhatsappNumber.checkIsNotEmpty(binding.edtWhatsappNumber)
            binding.tilPhoneNumber.checkIsNotEmpty(binding.edtPhoneNumber)
            when {
                binding.tilNik.error != null -> binding.edtNik.requestFocus()
                binding.tilFirstName.error != null -> binding.edtFirstName.requestFocus()
                binding.tilLastName.error != null -> binding.edtLastName.requestFocus()
                binding.tilDate.error != null -> binding.edtDate.requestFocus()
                binding.tilEmail.error != null -> binding.edtEmail.requestFocus()
                binding.tilUsername.error != null -> binding.edtUsername.requestFocus()
                binding.tilPassword.error != null -> binding.edtPassword.requestFocus()
                binding.tilPasswordConfirmation.error != null -> binding.edtPasswordConfirmation.requestFocus()
                binding.tilWhatsappNumber.error != null -> binding.edtWhatsappNumber.requestFocus()
                binding.tilPhoneNumber.error != null -> binding.edtPhoneNumber.requestFocus()
                else -> {
                    authViewModel.registerMechanic(
                        nik = binding.edtNik.text.toString(),
                        firstName = binding.edtFirstName.text.toString(),
                        lastName = binding.edtLastName.text.toString(),
                        birthDate = selectedDate,
                        joinDate = DateUtils.formatDate(Date(System.currentTimeMillis()), "yyyy-MM-dd"),
                        address = binding.edtDate.text.toString(),
                        whatsappNumber = binding.edtWhatsappNumber.text.toString(),
                        phoneNumber = binding.edtPhoneNumber.text.toString(),
                        email = binding.edtEmail.text.toString(),
                        username = binding.edtUsername.text.toString(),
                        password = binding.edtPassword.text.toString()
                    )
                }
            }
        }

        val dpd = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        dpd.addOnPositiveButtonClickListener {
            selectedDate = DateUtils.formatDate(Date(it), "yyyy-MM-dd")
            binding.edtDate.setText(DateUtils.formatDate(Date(it), "dd/MM/yyyy"))
        }

        binding.edtDate.setOnClickListener {
            dpd.show(supportFragmentManager, "")
        }
    }

    private fun initObserver() {
        authViewModel.registerMechanic.observe(this) {
            it.handleResult(onSuccess = {
                DialogUtils.showDialogInfoWithImage(
                    this, title = "Berhasil Daftar", message = "Berhasil",
                    positiveButton = Pair("OK") {
                        finish()
                    },
                    negativeButton = null
                )
            }, onFailure = { error ->
                DialogUtils.showDialogInfoWithImage(
                    this, title = "Gagal Daftar", message = error.errorData.message,
                    positiveButton = Pair("OK") {

                    },
                    negativeButton = null
                )
            })
        }
    }
}