package com.automa.ui.mechanic.account.certificate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.automa.domain.account.model.CertificationMechanicModel
import com.automa.ui.databinding.ItemCertificateMechanicBinding

class CertificateMechanicAdapter(
    private val list: MutableList<CertificationMechanicModel> = mutableListOf(),
    private val onDelete: (CertificationMechanicModel)->Unit,
    private val onEdit: (CertificationMechanicModel)->Unit
): RecyclerView.Adapter<CertificateMechanicAdapter.CertificateViewHolder>() {
    fun setData(data: List<CertificationMechanicModel>) {
        if (list.size>0) list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        val view = ItemCertificateMechanicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CertificateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class CertificateViewHolder(private val binding: ItemCertificateMechanicBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CertificationMechanicModel) {
            binding.tvCertificateName.text = item.name
            binding.tvCertDate.text = item.certDate
            binding.tvExpDate.text = item.certExpirationDate
            binding.tvDescription.text = item.desc
            binding.tvChip.text = item.level

            binding.btnDeleteCertificate.setOnClickListener { onDelete.invoke(item) }
            binding.btnEditCertificate.setOnClickListener { onEdit.invoke(item) }
        }
    }
}