package com.automa.ui.custom_component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.automa.ui.databinding.CustomToolbarTaskDetailBinding

class ToolbarTaskDetail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomToolbarTaskDetailBinding.inflate(LayoutInflater.from(context), this, true)
}