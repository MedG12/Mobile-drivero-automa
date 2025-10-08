package com.automa.ui.mechanic.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.mechanic_task.model.MechanicSubTaskModel
import com.automa.ui.databinding.ItemMechanicTaskDetailBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MechanicTaskDetailAdapter(
    private val data: MutableList<MechanicSubTaskModel> = mutableListOf(),
    private val onClick: (MechanicSubTaskModel)->Unit,
    private val onEdit: (MechanicSubTaskModel)->Unit,
): RecyclerView.Adapter<MechanicTaskDetailAdapter.SubTaskViewHolder>() {
    private var progressStatus = 0
    fun setProgressStatus(progressStatus: Int) {
        this.progressStatus = progressStatus
    }
    fun setData(list: List<MechanicSubTaskModel>) {
        if (data.size>0) data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskViewHolder {
        val view = ItemMechanicTaskDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubTaskViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class SubTaskViewHolder(private val binding: ItemMechanicTaskDetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MechanicSubTaskModel) {
            binding.tvSubTaskName.text = item.taskName
            binding.tvDescription.text = item.desc
            binding.tvCost.text = String.format("Rp ${formatCost(item.cost)}")
            binding.tvDuration.text = item.duration
            binding.root.setOnClickListener {
                onClick.invoke(item)
            }

            binding.ivEditSubTaskDetail.isVisible = progressStatus == 2 || progressStatus in 5..6

            binding.ivEditSubTaskDetail.setOnClickListener {
                onEdit.invoke(item)
            }
        }

        private fun formatCost(cost: Int): String {
            return try {
                val decimalFormat = DecimalFormat("#,###")
                decimalFormat.roundingMode = RoundingMode.DOWN
                decimalFormat.format(cost)
            } catch (e: Exception) {
                ""
            }
        }
    }
}