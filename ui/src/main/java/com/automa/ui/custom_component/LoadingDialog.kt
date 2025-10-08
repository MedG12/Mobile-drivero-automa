package com.automa.ui.custom_component

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.automa.ui.R
import com.automa.ui.databinding.CustomLoadingDialogBinding

open class LoadingDialog(context: Context): Dialog(context) {
    private val binding = CustomLoadingDialogBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(R.color.colorTransparent)
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }
}