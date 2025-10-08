package com.automa.ui.custom_component.check_box

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.Keep
import com.automa.ui.databinding.CustomCheckSheetItemBinding

class CheckBoxItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomCheckSheetItemBinding.inflate(LayoutInflater.from(context), this, true)

    private lateinit var checkSheetData: CheckBoxDataModel
    private var actionCheckedChangeListener: ((Boolean)->Unit) ?= null

    fun initView(data: CheckBoxDataModel) {
        checkSheetData = data
        binding.tvCheckBox.text = data.name
        binding.cbCheckSheet.isChecked = data.checked
        binding.cbCheckSheet.setOnCheckedChangeListener { _, b ->
            checkSheetData.checked = b
            actionCheckedChangeListener?.invoke(b)
        }
    }

    fun setOnCheckedChangeListener(action: (b: Boolean)->Unit) {
        this.actionCheckedChangeListener = action
    }

    fun getValue(): CheckBoxDataModel {
        return checkSheetData
    }
}

@Keep
data class CheckBoxDataModel(
    val name: String,
    var checked: Boolean
)