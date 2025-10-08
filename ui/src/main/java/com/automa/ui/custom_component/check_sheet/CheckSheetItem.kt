package com.automa.ui.custom_component.check_sheet

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isGone
import com.automa.domain.check_sheet.model.CheckSheetDetailModel
import com.automa.ui.databinding.CustomCheckSheetItemBinding

class CheckSheetItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomCheckSheetItemBinding.inflate(LayoutInflater.from(context), this, true)

    private lateinit var checkSheetData: CheckSheetDetailModel
    private var isChecked = 0

    init {
        binding.cbCheckSheet.setOnCheckedChangeListener { _, b ->
            isChecked = if (b) 1 else 0
//            binding.buttonReportCheckSheetItem.isGone = b
            binding.tilDescription.isGone = b
        }
    }

    fun initView(data: CheckSheetDetailModel) {
        checkSheetData = data
        binding.tvCheckBox.text = data.checkSheetDetailActivityName
        binding.edtDescription.setText(data.notes)
        isChecked = if (data.checked==-1) 0 else data.checked
        handleCheck()
    }

    fun getValue(): Triple<Int, Int, String> {
        return Triple(checkSheetData.id, isChecked, binding.edtDescription.text.toString())
    }

    fun isValid(): Boolean {
        return when {
            isChecked == 1 -> true
            isChecked == 0 && binding.edtDescription.text.toString().isNotEmpty() -> true
            isChecked == 0 && binding.edtDescription.text.toString().isEmpty() -> false
            else -> false
        }
    }

    private fun handleCheck() {
        binding.cbCheckSheet.isChecked = isChecked == 1
//        binding.buttonReportCheckSheetItem.isGone = isChecked == 1
        binding.tilDescription.isGone = isChecked == 1
    }
}