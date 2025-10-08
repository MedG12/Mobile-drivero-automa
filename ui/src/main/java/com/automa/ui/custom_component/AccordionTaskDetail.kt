package com.automa.ui.custom_component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.automa.domain.driver_task.model.WorkOrderFromModel
import com.automa.ui.R
import com.automa.ui.databinding.CustomAccordionTaskDetailBinding
import com.automa.ui.utils.copyToClipboard
import com.automa.ui.utils.isNotDefaultData

class AccordionTaskDetail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomAccordionTaskDetailBinding.inflate(LayoutInflater.from(context), this, true)

    private var isExpanded = false

    init {
        changeExpandState()

        binding.btnExpand.setOnClickListener {
            val currentState = isExpanded
            isExpanded = !currentState
            changeExpandState()
        }
    }

    private fun changeExpandState() {
        if (isExpanded) {
            binding.llAccordionContent.isGone = false
            binding.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        } else {
            binding.llAccordionContent.isGone = true
            binding.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }
    }

    fun setData(data: WorkOrderFromModel) {
        when (data.woSeq) {
            0 -> {
                binding.tvWorkOrderNumber.text = "Starting Point"
                hideBreakAndLoadingTime()
            }
            9989 -> {
                binding.tvWorkOrderNumber.text = "Home Point"
                hideBreakAndLoadingTime()
            }
            else -> {
                binding.tvWorkOrderNumber.text = data.woNumber
                binding.tvBreakTime.text = data.breakTime.toString()
                binding.tvLoadingTime.text = data.breakTime.toString()
            }
        }
        binding.tvWorkOrderDescription.text = data.woDesc
        binding.tvWorkOrderDestination.text = data.woDestinationName
        binding.tvEstDepartureTime.text = data.estDepartureTime
        binding.tvEstArrivalTime.text = data.estArrivalTime
        binding.tvPicName.text = data.picName
        binding.tvPicPhone.text = data.picPhone
        binding.btnCallPic.isVisible = data.picPhone.isNotDefaultData()
        binding.btnCallPic.setOnClickListener { copyToClipboard(context, data.picPhone) }
    }

    fun setButtonUploadClick(action: ()->Unit) {
        binding.btnUploadPod.setOnClickListener { action.invoke() }
    }

    private fun hideBreakAndLoadingTime() {
        binding.tvLabelBreakTime.isGone = true
        binding.tvLabelLoadingTime.isGone = true
        binding.tvBreakTime.isGone = true
        binding.tvLoadingTime.isGone = true
    }
}