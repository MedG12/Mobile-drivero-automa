package com.automa.ui.mechanic.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.mechanic_task.model.MechanicTaskItemModel
import com.automa.ui.databinding.ItemMechanicTaskListBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MechanicTaskAdapter(
    private val list: MutableList<MechanicTaskItemModel> = mutableListOf(),
    private val onClick: (MechanicTaskItemModel)->Unit,
    private val onUploadProof: (MechanicTaskItemModel)->Unit
): RecyclerView.Adapter<MechanicTaskAdapter.MechanicTaskDetailViewHolder>() {
    private var parentTaskStatus = -1
    private var workshopName = ""
    private var roleHeadMechanic = false
    fun setWorkshop(workshop: String) {
        this.workshopName = workshop
    }
    fun setRoleHeadMechanic(isHeadMechanic: Boolean) {
        this.roleHeadMechanic = isHeadMechanic
    }
    fun setTaskStatus(status: Int) {
        parentTaskStatus = status
    }
    fun setData(data: List<MechanicTaskItemModel>) {
        if (list.size > 0) {
            list.clear()
        }
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MechanicTaskDetailViewHolder {
        val view = ItemMechanicTaskListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MechanicTaskDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: MechanicTaskDetailViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class MechanicTaskDetailViewHolder(private val binding: ItemMechanicTaskListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MechanicTaskItemModel) {
            binding.tvMaintenanceNumber.text = item.taskName
            binding.tvDescription.text = item.desc
            binding.tvMechanicName.text = String.format("${item.mechanicFirstName} ${item.mechanicLastName}")
            binding.tvScheduledTime.text = item.scheduledDateTime
            binding.tvDuration.text = String.format("${item.totalDuration} Menit")
            binding.tvWorkshop.text = workshopName
            binding.btnFillDetail.isVisible = parentTaskStatus >= 2
            binding.btnUploadProof.isVisible = parentTaskStatus >= 5

            if (parentTaskStatus == 2 || parentTaskStatus in 5..6) {
                binding.btnFillDetail.text = "ISI DETAIL"
            } else {
                binding.btnFillDetail.text = "LIHAT DETAIL"
            }
            when (parentTaskStatus) {
                5 -> binding.btnUploadProof.text = "TAMBAHKAN BUKTI"
                else -> binding.btnUploadProof.text = "LIHAT BUKTI"
            }
            binding.btnFillDetail.setOnClickListener {
                onClick.invoke(item)
            }
            binding.btnUploadProof.setOnClickListener {
                onUploadProof.invoke(item)
            }
            binding.btnAddMechanic.isGone = !roleHeadMechanic
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