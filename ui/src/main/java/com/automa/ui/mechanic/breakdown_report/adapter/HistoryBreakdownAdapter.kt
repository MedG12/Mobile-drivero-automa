package com.automa.ui.mechanic.breakdown_report.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.breakdown_report.model.BreakdownReportItemModel
import com.automa.ui.R
import com.automa.ui.databinding.ItemHistoryBreakdownBinding
import com.automa.ui.utils.DateUtils

class HistoryBreakdownAdapter(
    private val data: MutableList<BreakdownReportItemModel> = mutableListOf(),
    private val onClick: (BreakdownReportItemModel)->Unit
): RecyclerView.Adapter<HistoryBreakdownAdapter.HistoryBreakdownViewHolder>() {
    fun setData(list: List<BreakdownReportItemModel>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryBreakdownViewHolder {
        val view = ItemHistoryBreakdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryBreakdownViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryBreakdownViewHolder, position: Int) {
        val itemData = data[position]
        holder.bind(itemData)
    }

    inner class HistoryBreakdownViewHolder(private val binding: ItemHistoryBreakdownBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BreakdownReportItemModel) {
            binding.tvIdBreakdown.text = item.name
            binding.tvCondition.text = item.desc
            binding.tvUrgency.text = when (item.level) {
                "1" -> binding.root.context.getString(R.string.label_urgency_high)
                "2" -> binding.root.context.getString(R.string.label_urgency_medium)
                "3" -> binding.root.context.getString(R.string.label_urgency_low)
                else -> "-"
            }
            binding.tvFleetPlateNumber.text = item.regNumber
            binding.tagsCategory.setTags(listOf(item.mainCategorical), showRemoveButton = false)
            binding.tagsSubcategory.setTags(listOf(item.subCategoricalName), showRemoveButton = false)
            val statusList = binding.root.context.resources.getStringArray(R.array.status_breakdown_report).toList()
            if (item.idStatus in 0..4) {
                binding.tvStatus.text = statusList[item.idStatus]
            }
            when (item.idStatus) {
                0 -> binding.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.error500))
                1 -> binding.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.neutral500))
                2 -> binding.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary500))
                3 -> binding.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary500))
                4 -> binding.tvStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.success500))
                else -> Unit
            }
            binding.tvReportedAt.text = DateUtils.formatDate(item.createdOn,"yyyy-MM-dd HH:mm:ss.SSSSSS", "yyyy-MM-dd HH:mm:ss")
            binding.btnViewPhotoCondition.setOnClickListener {
                onClick.invoke(item)
            }
            binding.tvLabelStoring.isVisible = item.isStoring
            binding.tvStoringReason.isVisible = item.isStoring
            binding.tvStoringReason.text = item.storingReason
        }
    }
}