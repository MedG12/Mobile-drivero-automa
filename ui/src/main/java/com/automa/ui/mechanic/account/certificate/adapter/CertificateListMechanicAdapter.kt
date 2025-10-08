package com.automa.ui.mechanic.account.certificate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.account.model.CertificationMechanicModel
import com.automa.ui.databinding.ItemCertificateListMechanicBinding

class CertificateListMechanicAdapter(
    private val list: MutableList<CertificationMechanicModel> = mutableListOf()
): RecyclerView.Adapter<CertificateListMechanicAdapter.CertificateListViewHolder>() {
    fun setData(data: List<CertificationMechanicModel>) {
        if (list.size>0) list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateListViewHolder {
        val view = ItemCertificateListMechanicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CertificateListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertificateListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class CertificateListViewHolder(private val binding: ItemCertificateListMechanicBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CertificationMechanicModel) {
            binding.tvCertificateName.text = item.name
            binding.tvCertDate.text = item.certDate
            binding.tvExpDate.text = item.certExpirationDate
            binding.tvChip.text = item.level
        }
    }
}