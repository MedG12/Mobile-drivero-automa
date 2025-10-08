package com.automa.ui.custom_component.check_sheet

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.automa.domain.check_sheet.model.CheckSheetDetailModel
import com.automa.ui.databinding.CustomCheckSheetBinding

class CheckSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomCheckSheetBinding.inflate(LayoutInflater.from(context), this, true)

    private val itemViews = mutableListOf<CheckSheetItem>()

    fun setData(data: List<CheckSheetDetailModel>) {
        if (data.isNotEmpty()) {
            binding.tvCheckSheetName.text = data.first().checkSheetName
        }
        data.forEach {
            val view = CheckSheetItem(context)
            view.initView(it)
            itemViews.add(view)
            binding.llCheckSheetContent.addView(view)
        }
    }

    fun getData(): List<Triple<Int, Int, String>> {
        val list = mutableListOf<Triple<Int, Int, String>>()
        itemViews.forEach {
            list.add(it.getValue())
        }
        return list
    }

    fun checkIsValid(): Boolean {
        val listValid = mutableListOf<Boolean>()
        itemViews.forEach {
            listValid.add(it.isValid())
        }
        return !listValid.contains(false)
    }
}