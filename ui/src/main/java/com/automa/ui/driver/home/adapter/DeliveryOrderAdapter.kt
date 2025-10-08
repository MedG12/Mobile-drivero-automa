package com.automa.ui.driver.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.ui.R
import com.automa.ui.databinding.ItemDoTaskBinding

class DeliveryOrderAdapter(
    private val list: MutableList<DeliveryOrderModel> = mutableListOf(),
    private val listener: DeliveryOrderClickListener
): RecyclerView.Adapter<DeliveryOrderAdapter.DeliveryOrderViewHolder>() {
    private var filteredData = listOf<DeliveryOrderModel>()
    private var currentActiveTask = -1
    private var currentExpandedPosition = -1
    private var allowDuplicateDO = false
    private var taskType = TYPE_DELIVERY_ORDER

    companion object {
        const val TYPE_DELIVERY_ORDER = 0
        const val TYPE_MASTER_DATA_DO = 1
        const val TYPE_DELIVERY_ORDER_DONE = 2
    }

    fun setTaskType(type: Int) {
        taskType = type
    }

    fun resetExpandedPosition() {
        currentExpandedPosition = -1
    }

    fun setCurrentActiveTask(id: Int) {
        currentActiveTask = id
        notifyDataSetChanged()
    }

    fun setAllowDuplicateDO(boolean: Boolean) {
        this.allowDuplicateDO = boolean
    }

    fun setData(listData: List<DeliveryOrderModel>) {
        if (list.size>0) list.clear()
        list.addAll(listData)
        filteredData = list.toList()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        if (list.isEmpty()) return
        filteredData = if (query.isNotBlank()) {
            list.filter { it.deliveryOrderNumber.contains(query, ignoreCase = true) }
        } else {
            list
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = filteredData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryOrderViewHolder {
        val view = ItemDoTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryOrderViewHolder, position: Int) {
        val itemData = filteredData[position]
        holder.bind(itemData)
    }

    inner class DeliveryOrderViewHolder(private val binding: ItemDoTaskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DeliveryOrderModel) {
            binding.tvDONumber.text = item.deliveryOrderNumber
            binding.tvDODesc.text = item.deliveryOrderDesc
            binding.tvAssignedDate.text = item.assignedDate
            binding.tvFleetPlateNumber.text = item.fleetPlate
            binding.btnDuplicate.isVisible = allowDuplicateDO

            binding.btnDuplicate.setOnClickListener {
                listener.onDuplicateClicked(item, taskType)
            }
            binding.btnAcceptTask.setOnClickListener {
                listener.onItemClicked(item, taskType)
            }
            binding.root.setOnLongClickListener {
                if (taskType == TYPE_DELIVERY_ORDER) {
                    listener.onLongClicked(item)
                    true
                } else false
            }
            binding.tvViewTask.setOnClickListener {
                updateState()
            }

            if (adapterPosition == currentExpandedPosition) {
                binding.tvViewTask.text = binding.root.context.getString(R.string.text_close)
                binding.clContent.isGone = false
            } else {
                binding.tvViewTask.text = binding.root.context.getString(R.string.label_view)
                binding.clContent.isGone = true
            }

            when (taskType) {
                TYPE_DELIVERY_ORDER -> initItemDeliveryOrder(item)
                TYPE_DELIVERY_ORDER_DONE -> initItemDeliveryOrderDone(item)
                TYPE_MASTER_DATA_DO -> initItemMasterData(item)
            }
        }

        private fun updateState() {
            notifyItemChanged(currentExpandedPosition)
            currentExpandedPosition = if (currentExpandedPosition == adapterPosition) {
                -1
            } else {
                adapterPosition
            }
            notifyItemChanged(currentExpandedPosition)
        }

        private fun initItemDeliveryOrder(item: DeliveryOrderModel) {
            binding.btnAcceptTask.isGone = false
            binding.chipSuccess.isGone = true
            binding.tvLabelPlateFleet.isGone = false
            binding.tvFleetPlateNumber.isGone = false
            when {
                item.id == currentActiveTask -> {
                    binding.clContainer.isEnabled = true
                    binding.tvViewTask.isClickable = true
                    binding.tvDONumber.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary500))
                    binding.tvViewTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary500))
                    binding.btnAcceptTask.text = binding.root.context.getString(R.string.button_track_task)
                }
                currentActiveTask == -1 -> {
                    binding.clContainer.isEnabled = true
                    binding.tvViewTask.isClickable = true
                    binding.tvDONumber.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary500))
                    binding.tvViewTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary500))
                    binding.btnAcceptTask.text = binding.root.context.getString(R.string.button_accept_task)
                }
                else -> {
                    binding.clContainer.isEnabled = false
                    binding.tvViewTask.isClickable = false
                    binding.tvDONumber.setTextColor(ContextCompat.getColor(binding.root.context, R.color.neutral300))
                    binding.tvViewTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.neutral300))
                    binding.btnAcceptTask.text = binding.root.context.getString(R.string.button_accept_task)
                }
            }
        }

        private fun initItemDeliveryOrderDone(item: DeliveryOrderModel) {
            binding.btnAcceptTask.isGone = false
            binding.chipSuccess.isGone = false
            binding.tvLabelPlateFleet.isGone = false
            binding.tvFleetPlateNumber.isGone = false
            binding.btnAcceptTask.text = binding.root.context.getString(R.string.button_track_task)
        }

        private fun initItemMasterData(item: DeliveryOrderModel) {
            binding.btnAcceptTask.isGone = true
            binding.chipSuccess.isGone = true
            binding.tvLabelPlateFleet.isGone = true
            binding.tvFleetPlateNumber.isGone = true
        }
    }

    interface DeliveryOrderClickListener {
        fun onItemClicked(data: DeliveryOrderModel, type: Int)
        fun onLongClicked(data: DeliveryOrderModel)
        fun onDuplicateClicked(data: DeliveryOrderModel, type: Int)
    }
}