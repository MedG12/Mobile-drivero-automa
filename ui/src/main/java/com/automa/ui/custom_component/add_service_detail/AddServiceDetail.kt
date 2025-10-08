package com.automa.ui.custom_component.add_service_detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.automa.ui.databinding.CustomAddServiceDetailBinding

class AddServiceDetail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomAddServiceDetailBinding.inflate(LayoutInflater.from(context), this, true)
    private val itemViews = mutableListOf<AddServiceDetailItem>()

    fun addServiceDetail() {
        val view = AddServiceDetailItem(context)
        view.initView {
            itemViews.remove(view)
            binding.llAddServiceDetailContent.removeView(view)
        }
        itemViews.add(view)
        binding.llAddServiceDetailContent.addView(view)
    }
}