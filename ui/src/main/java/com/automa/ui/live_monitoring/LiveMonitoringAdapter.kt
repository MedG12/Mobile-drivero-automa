package com.automa.ui.live_monitoring

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.live_monitoring.model.LiveMonitoringModel
import com.automa.ui.R
import com.automa.ui.databinding.ItemLiveMonitoringBinding
import com.automa.ui.utils.DateUtils
import com.automa.ui.utils.isNotDefaultData
import java.lang.Exception
import java.util.Date

class LiveMonitoringAdapter(
    private val data: MutableList<LiveMonitoringModel> = mutableListOf(),
    private val action: ItemLiveMonitoringClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var filteredData = listOf<LiveMonitoringModel>()

    fun setData(list: List<LiveMonitoringModel>) {
        data.clear()
        data.addAll(list)
        filteredData = data.toList()
        notifyDataSetChanged()
    }

    fun filter(query: String): List<LiveMonitoringModel> {
        if (data.isEmpty()) return emptyList()
        filteredData = if (query.isNotBlank()) {
            data.filter { it.regNumberWithDoor.contains(query, ignoreCase = true) }
        } else {
            data
        }
        notifyDataSetChanged()
        return filteredData
    }

    override fun getItemCount(): Int = filteredData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemLiveMonitoringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LiveMonitoringViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LiveMonitoringViewHolder).bind(filteredData[position])
    }

    inner class LiveMonitoringViewHolder(private val binding: ItemLiveMonitoringBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LiveMonitoringModel) {
            binding.tvFleetId.text = item.regNumberWithDoor
            binding.tvLastUpdated.text = DateUtils.formatDate(item.lastUpdateWithSpeed, DateUtils.DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm")

            //Indicator Color
            if (item.lastUpdateWithSpeed.isNotDefaultData()) {
                try {
                    val long = DateUtils.parseDate(item.lastUpdateWithSpeed, DateUtils.DEFAULT_DATE_FORMAT)
                    val currentTimeLong = Date().time
                    if (currentTimeLong > long) {
                        val calc : Double = (currentTimeLong - long).toDouble().div(60)
                        when {
                            calc > 0.0 && calc <= 15.0 -> {
                                binding.llChip.isInvisible = false
                                binding.llChip.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.success200))
                                binding.tvChip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.success500))
                                binding.tvChip.text = binding.root.context.getString(R.string.label_fleet_online)
                            }
                            calc > 15.0 && calc <= 60 -> {
                                binding.llChip.isInvisible = false
                                binding.llChip.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.warning200))
                                binding.tvChip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.warning500))
                                binding.tvChip.text = binding.root.context.getString(R.string.label_fleet_online)
                            }
                            calc > 60 -> {
                                binding.llChip.isInvisible = false
                                binding.llChip.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.error200))
                                binding.tvChip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.error500))
                                binding.tvChip.text = binding.root.context.getString(R.string.label_fleet_offline)
                            }
                            else -> {
                                binding.llChip.isInvisible = true
                            }
                        }
                    } else {
                        binding.llChip.isInvisible = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.llChip.isInvisible = true
                }
            } else {
                binding.llChip.isInvisible = true
            }

            //Position
            if (item.lat.isNotDefaultData() && item.lng.isNotDefaultData()) {
                binding.tvPosition.text = String.format("${item.lat}, ${item.lng}")
                binding.tvOpenMaps.isVisible = true
            } else {
                binding.tvPosition.text = "-"
                binding.tvOpenMaps.isVisible = false
            }

            //Status
            if (item.wrn.isNotDefaultData() && item.spd.isNotDefaultData()) {
                when {
                    item.wrn == 0 && item.spd > 0 -> {
                        binding.tvStatus.text = "Off (Warning!)"
                    }
                    item.wrn == 0 -> {
                        binding.tvStatus.text = "Off"
                    }
                    item.wrn == 1 && item.spd > 0 -> {
                        binding.tvStatus.text = "On & Moving"
                    }
                    item.wrn == 1 && item.spd == 0 -> {
                        binding.tvStatus.text = "On & Idle"
                    }
                }
            } else {
                binding.tvStatus.text = "-"
            }

            binding.root.setOnClickListener {
                action.onClicked(item)
            }

            binding.tvOpenMaps.setOnClickListener {
                action.onOpenMap(item)
            }
        }
    }
}

interface ItemLiveMonitoringClickListener {
    fun onClicked(item: LiveMonitoringModel)
    fun onOpenMap(item: LiveMonitoringModel)
}