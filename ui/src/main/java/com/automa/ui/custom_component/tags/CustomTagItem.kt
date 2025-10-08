package com.automa.ui.custom_component.tags

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isGone
import com.automa.ui.databinding.CustomTagItemBinding

class CustomTagItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttributes: Int = 0
): LinearLayout(context, attrs, defStyleAttributes) {
    private val binding = CustomTagItemBinding.inflate(LayoutInflater.from(context), this, true)

    fun initView(name: String, onRemove: ()->Unit) {
        binding.tvTagText.text = name
        binding.ivRemoveTag.setOnClickListener { onRemove.invoke() }
    }

    fun getText(): String {
        return binding.tvTagText.text.toString()
    }

    fun showRemoveButton(show: Boolean) {
        if (show.not()) {
            binding.ivRemoveTag.isGone = true
        }
    }
}