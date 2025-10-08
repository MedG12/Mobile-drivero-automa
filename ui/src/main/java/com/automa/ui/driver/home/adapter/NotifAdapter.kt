package com.automa.ui.driver.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.automa.ui.databinding.ItemNotifBinding

class NotifAdapter(
    private val list: MutableList<String> = mutableListOf()
): RecyclerView.Adapter<NotifAdapter.NotifViewHolder>() {

    fun setData(data: List<String>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = ItemNotifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        val itemData = list[position]
        holder.bind(itemData)
    }

    inner class NotifViewHolder(private val binding: ItemNotifBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvNotif.text = item
        }
    }
}