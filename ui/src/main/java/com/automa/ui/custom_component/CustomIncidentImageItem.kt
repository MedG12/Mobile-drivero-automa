package com.automa.ui.custom_component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isGone
import com.automa.ui.databinding.CustomIncidentImageItemBinding
import com.bumptech.glide.Glide

class CustomIncidentImageItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomIncidentImageItemBinding.inflate(LayoutInflater.from(context), this, true)

    fun initView(imagePath: String, onEdit: ()-> Unit, onDelete: ()-> Unit, parentView: View) {
        Glide.with(context).load(imagePath).into(binding.ivImageIncident)
        binding.ivOption.setOnClickListener {
            CustomIncidentImagePopupWindow(parentView)
                .init(onEdit, onDelete, binding.ivImageIncident)
                .show()
        }
    }

    fun setEditable(boolean: Boolean) {
        binding.ivOption.isGone = boolean.not()
    }
}