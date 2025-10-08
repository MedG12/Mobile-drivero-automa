package com.automa.ui.mechanic.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.ui.databinding.ItemMechanicTaskBinding
import com.automa.ui.utils.DateUtils
import com.automa.ui.utils.MechanicTaskHelper
import com.automa.ui.utils.isNotDefaultData
import java.text.SimpleDateFormat
import java.util.Date

class HomeMechanicTaskAdapter(
    private val list: MutableList<MechanicTaskModel> = mutableListOf(),
    private val onClick: (data: MechanicTaskModel)->Unit,
    private val onEmptyData: (Boolean)->Unit
): RecyclerView.Adapter<HomeMechanicTaskAdapter.TaskViewHolder>() {
    private var workshopName = ""
    private val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private var filterToday = false
    private val allData = mutableListOf<MechanicTaskModel>()

    fun setFilterToday(boolean: Boolean) {
        this.filterToday = boolean
        list.clear()
        list.addAll(sortData(allData))
        onEmptyData.invoke(list.isEmpty())
        notifyDataSetChanged()
    }

    fun setData(data: List<MechanicTaskModel>) {
        if (list.size > 0) {
            allData.clear()
            list.clear()
        }
        allData.addAll(data)
        list.addAll(sortData(data))
        notifyDataSetChanged()
    }

    fun addData(data: List<MechanicTaskModel>) {
        val newList = mutableListOf<MechanicTaskModel>()
        newList.addAll(list)
        newList.addAll(data)
        list.clear()
        allData.addAll(data)
        list.addAll(sortData(newList))
        notifyDataSetChanged()
    }

    fun setWorkshopName(name: String) {
        this.workshopName = name
    }

    private fun sortData(data: List<MechanicTaskModel>): List<MechanicTaskModel> {
        return try {
            val sortedData = data.sortedByDescending {
                val parsedDate = sourceFormat.parse(it.scheduledDatetime)
                parsedDate?.time
            }
            if (filterToday) {
                val currentDate = DateUtils.formatDate(Date(), "yyyy-MM-dd")
                sortedData.filter {
                    it.scheduledDatetime.contains(currentDate)
                }
            } else {
                sortedData
            }
        } catch (e: Exception) {
            data
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = ItemMechanicTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val itemData = list[position]
        holder.bind(itemData)
    }

    inner class TaskViewHolder(private val binding: ItemMechanicTaskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MechanicTaskModel) {
            binding.tvMaintenanceNumber.text = item.maintenanceNumber
            binding.tvDescription.text = item.maintenanceTitle
            val fleetString = if (item.fleetRegNumber.isNotDefaultData()) item.fleetRegNumber else item.fleetNotes
            binding.tvFleetPlateNumber.text = fleetString
            val workshopString = if (item.workshopName.isNotDefaultData()) item.workshopName else workshopName
            binding.tvWorkshop.text = workshopString
            binding.tvAssignedDate.text = item.scheduledDatetime
            binding.tvChip.text = String.format("${item.progressStatus}-${MechanicTaskHelper.getStatusName(item.progressStatus)}")
            binding.root.setOnClickListener {
                onClick.invoke(item)
            }
            binding.btnFillDetail.isGone = true
        }
    }
}