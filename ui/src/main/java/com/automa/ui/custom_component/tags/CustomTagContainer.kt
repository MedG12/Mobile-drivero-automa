package com.automa.ui.custom_component.tags

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import com.automa.ui.databinding.CustomTagContainerBinding

class CustomTagContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttributes: Int = 0
): ConstraintLayout(context, attrs, defStyleAttributes) {
    private val binding = CustomTagContainerBinding.inflate(LayoutInflater.from(context), this, true)

    private val itemViews = mutableListOf<CustomTagItem>()

    fun setTags(listTag: List<String>, showRemoveButton: Boolean = true) {
        itemViews.clear()
        listTag.forEach {
            val tag = CustomTagItem(context)
            tag.id = View.generateViewId()
            tag.initView(it) {
                binding.clTags.removeView(tag)
                itemViews.remove(tag)
            }
            tag.showRemoveButton(showRemoveButton)
            itemViews.add(tag)
            binding.clTags.addView(tag)
        }
        updateFlowChain()
    }

    private fun updateFlowChain() {
        val listInt = mutableListOf<Int>()
        itemViews.forEach {
            listInt.add(it.id)
        }
        binding.flowTags.apply {
            this.setWrapMode(Flow.WRAP_CHAIN)
        }
        binding.flowTags.referencedIds = listInt.toIntArray()
        binding.clTags.invalidate()
    }

    fun getTags(): List<String> {
        val list = mutableListOf<String>()
        itemViews.forEach {
            list.add(it.getText())
        }
        return list.toList()
    }
}