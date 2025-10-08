package com.automa.ui.shared.auth

import android.os.Bundle
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityForgotPasswordBinding
import com.automa.ui.utils.DialogUtils

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgotPassword.setOnClickListener {
            if (binding.edtUsername.text.toString().isBlank()) {
                DialogUtils.showDialogForgotPassword(this, false) {

                }
            } else {
                DialogUtils.showDialogForgotPassword(this, true) {

                }
            }
        }
    }
}