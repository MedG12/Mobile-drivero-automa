package com.automa.ui.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.util.PatternsCompat
import com.automa.ui.R
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.onTextEmailValidation(edtView: EditText) {
    edtView.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            edtView.post{
                this@onTextEmailValidation.checkEmailValidation(edtView)
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}

fun TextInputLayout.onTextChangeConfirmPasswordValidation(edtView: EditText, edtMatcher: EditText) {
    edtView.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            edtView.post{
                this@onTextChangeConfirmPasswordValidation.checkConfirmationPassword(edtView, edtMatcher)
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}

fun TextInputLayout.checkConfirmationPassword(edtView: EditText, edtMatcher: EditText) {
    if (edtView.text.toString() == edtMatcher.text.toString()) {
        this.error = null
        this.clearError()
    }
    else this.error = edtView.context.getString(R.string.password_mismatch)
}

fun TextInputLayout.onTextValidation(edtView: EditText) {
    edtView.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            edtView.post {
                this@onTextValidation.checkIsNotEmpty(edtView)
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}

fun TextInputLayout.onTextMinimumValidation(edtView: EditText, minLength: Int) {
    edtView.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            edtView.post {
                this@onTextMinimumValidation.checkMinimumCharacter(edtView, minLength)
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}

fun TextInputLayout.checkEmailValidation(edtView: EditText) {
    val regex = PatternsCompat.EMAIL_ADDRESS
    if (regex.matcher(edtView.text.toString()).matches()){
        this.error = null
        this.clearError()
    } else if (edtView.text.toString().isBlank()) {
        this.error = edtView.context.getString(R.string.must_not_empty)
    } else {
        this.error = edtView.context.getString(R.string.invalid_email_format)
    }
}

fun TextInputLayout.checkIsNotEmpty(edtView: EditText) {
    if (edtView.text.isNullOrEmpty()){
        this.error = edtView.context.getString(R.string.must_not_empty)
    } else {
        this.error = null
        this.clearError()
    }
}

fun TextInputLayout.checkMinimumCharacter(edtView: EditText, minLength: Int){
    when {
        edtView.text.isNullOrEmpty() -> this.error = edtView.context.getString(R.string.must_not_empty)
        edtView.text.toString().length < minLength -> this.error = edtView.context.getString(R.string.message_minimum_length, minLength)
        else -> {
            this.error = null
            this.clearError()
        }
    }
}

fun TextInputLayout.clearError(){
    this.isErrorEnabled = false
}