package com.automa.ui.custom_component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isGone
import com.automa.domain.breakdown_report.model.BreakdownReportPhotoModel
import com.automa.domain.driver_task.model.ImagePodItemModel
import com.automa.ui.R
import com.automa.ui.databinding.CustomAccordionPodBinding
import com.automa.ui.utils.DateUtils
import com.bumptech.glide.Glide

class AccordionPod @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomAccordionPodBinding.inflate(LayoutInflater.from(context), this, true)

    private var isExpanded = false

    init {
        changeExpandState()

        binding.ivExpand.setOnClickListener {
            val currentState = isExpanded
            isExpanded = !currentState
            changeExpandState()
        }
    }

    private fun changeExpandState() {
        if (isExpanded) {
            binding.clContentPod.isGone = false
            binding.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        } else {
            binding.clContentPod.isGone = true
            binding.ivExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }
    }

    fun setData(doNumber: String, fleetPlate: String, data: ImagePodItemModel) {
        binding.tvDoNumber.text = doNumber
        binding.tvFleetPlateNumber.text = fleetPlate
        binding.tvDescription.text = data.desc
        binding.tvCreatedOn.text = data.createdOn
        Glide.with(context).load(data.link).into(binding.ivPod)
    }

    fun setData(doNumber: String, fleetPlate: String, data: BreakdownReportPhotoModel) {
        binding.tvDoNumber.text = doNumber
        binding.tvFleetPlateNumber.text = fleetPlate
        binding.tvDescription.text = data.desc
        binding.tvLabelCreatedOn.text = context.getString(R.string.label_reported_at)
        binding.tvCreatedOn.text = DateUtils.formatDate(data.createdOn, DateUtils.DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm:ss")
        Glide.with(context).load(data.link).into(binding.ivPod)
    }
}