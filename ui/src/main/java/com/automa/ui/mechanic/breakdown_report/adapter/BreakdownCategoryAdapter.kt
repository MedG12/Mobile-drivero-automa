package com.automa.ui.mechanic.breakdown_report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel
import com.automa.ui.databinding.CustomCheckSheetItemBinding

class BreakdownCategoryAdapter(
    private val data: MutableList<BreakdownSubCategoryModel> = mutableListOf()
): RecyclerView.Adapter<BreakdownCategoryAdapter.CategoryViewHolder>() {
    private var checkedCategory: BreakdownSubCategoryModel ?= null

    fun setData(list: List<BreakdownSubCategoryModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun getSelectedCategory(): BreakdownSubCategoryModel? {
        return checkedCategory
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakdownCategoryAdapter.CategoryViewHolder {
        val view = CustomCheckSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreakdownCategoryAdapter.CategoryViewHolder, position: Int) {
        val itemData = data[position]
        holder.bind(itemData)
    }

    inner class CategoryViewHolder(private val binding: CustomCheckSheetItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BreakdownSubCategoryModel) {
            binding.tvCheckBox.text = item.name
            if (checkedCategory != null) {
                binding.cbCheckSheet.isChecked = checkedCategory?.name.equals(item.name, true)
            }
            binding.cbCheckSheet.setOnClickListener {
                updateCheckedState(item, binding.cbCheckSheet.isChecked)
            }
        }

        private fun updateCheckedState(data: BreakdownSubCategoryModel, isChecked: Boolean) {
            if (isChecked) {
                checkedCategory = null
                notifyDataSetChanged()
                checkedCategory = data
                notifyDataSetChanged()
            } else {
                checkedCategory = null
                notifyDataSetChanged()
            }
        }
    }
}