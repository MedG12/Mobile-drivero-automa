package com.automa.ui.driver.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.ui.R
import com.automa.ui.databinding.ItemDoTaskBinding

class FinishedDeliveryOrderAdapter(
    private val list: MutableList<DeliveryOrderModel> = mutableListOf(),
    private val listener: DeliveryOrderClickListener
): RecyclerView.Adapter<FinishedDeliveryOrderAdapter.DeliveryOrderViewHolder>() {
    private var filteredData = listOf<DeliveryOrderModel>()

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
        private var isExpanded = false
        fun bind(item: DeliveryOrderModel) {
            binding.tvDONumber.text = item.deliveryOrderNumber
            binding.tvDODesc.text = item.deliveryOrderDesc
            binding.tvAssignedDate.text = item.assignedDate
            binding.tvFleetPlateNumber.text = item.fleetPlate
            updateState(isExpanded)
            binding.btnAcceptTask.setOnClickListener {
                listener.onItemClicked(item)
            }
            binding.chipSuccess.isGone = false
            binding.tvViewTask.isGone = true
            binding.tvViewTask.setOnClickListener {
                isExpanded = !isExpanded
                updateState(isExpanded)
            }
        }

        private fun updateState(isExpanded: Boolean) {
            binding.clContent.isGone = !isExpanded
            binding.tvViewTask.text = if (isExpanded) binding.root.context.getString(R.string.text_close) else binding.root.context.getString(R.string.label_view)
        }
    }

    interface DeliveryOrderClickListener {
        fun onItemClicked(data: DeliveryOrderModel)
    }
}