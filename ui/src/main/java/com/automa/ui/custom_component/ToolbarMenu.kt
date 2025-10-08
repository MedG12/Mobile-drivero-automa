package com.automa.ui.custom_component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isGone
import com.automa.ui.R
import com.automa.ui.databinding.LayoutToolbarMenuBinding

class ToolbarMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = LayoutToolbarMenuBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.btnShowMenu.imageTintList = ColorStateList.valueOf(Color.WHITE)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolbarMenu)
        val icon = typedArray.getDrawable(R.styleable.ToolbarMenu_toolbarIcon)
        if (icon != null) {
            binding.ivLogoToolbar.setImageDrawable(icon)
        }
        typedArray.recycle()
        binding.btnShowMenu.isGone = true
    }

    fun setOnShowMenuClickListener(action: ()->Unit) {
        binding.btnShowMenu.setOnClickListener {
            action.invoke()
        }
    }
}