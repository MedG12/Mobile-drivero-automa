package com.automa.ui.custom_component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.updateLayoutParams
import com.automa.ui.R
import com.automa.ui.databinding.CustomToolbarBasicBinding

class ToolbarBasic @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomToolbarBasicBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolbarBasic)
        val text = typedArray.getString(R.styleable.ToolbarBasic_toolbarText)
        binding.tvToolbarTitle.text = text
        typedArray.recycle()
    }

    fun setToolbarText(text: String) {
        binding.tvToolbarTitle.text = text
    }

    fun setOnNavigationClickListener(action: ()->Unit) {
        binding.ivToolbarBack.setOnClickListener {
            action.invoke()
        }
    }

    fun setRightIcons(icons: List<Pair<Drawable?, ()->Unit>>) {
        binding.llIcons.removeAllViews()
        icons.forEach { item ->
            val iv = AppCompatImageView(context)
            iv.setImageDrawable(item.first)
            iv.setOnClickListener { item.second.invoke() }
            binding.llIcons.addView(iv)
            iv.updateLayoutParams<LayoutParams> {
                width = LayoutParams.WRAP_CONTENT
                height = LayoutParams.WRAP_CONTENT
            }
        }
    }
}