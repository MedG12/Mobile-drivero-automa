package com.automa.ui.custom_component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.fragment.app.FragmentManager
import com.automa.domain.mechanic_task.model.MechanicTaskProofModel
import com.automa.ui.databinding.CustomUploadPhotoBoxBinding
import com.automa.ui.shared.photo_detail.PhotoDetailDialogFragment
import com.automa.ui.utils.toast

class TaskProofPhotoBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttributes: Int = 0
): LinearLayout(context, attrs, defStyleAttributes) {
    private val binding = CustomUploadPhotoBoxBinding.inflate(LayoutInflater.from(context), this, true)

    private var maxPhotos = 3
    private var canUpload = false
    private val listProof = mutableListOf<MechanicTaskProofModel>()
    private var parentView: View ?= null
    private var onDeleteAction: ((MechanicTaskProofModel)->Unit) ?= null
    private var fragmentManager: FragmentManager ?= null

    fun setFragmentManager(fm: FragmentManager) {
        this.fragmentManager = fm
    }

    fun setAnchor(v: View) {
        this.parentView = v
    }

    fun setMaxPhotos(max: Int) {
        this.maxPhotos = max
    }

    fun setCanUpload(boolean: Boolean) {
        this.canUpload = boolean
        binding.llUploadPhoto.isGone = canUpload.not()
    }

    fun setOnUploadClick(action: ()->Unit) {
        binding.llUploadPhoto.setOnClickListener {
            action.invoke()
        }
    }

    fun setOnDelete(action: (MechanicTaskProofModel) -> Unit) {
        this.onDeleteAction = action
    }

    fun setData(list: List<MechanicTaskProofModel>) {
        if (list.isNotEmpty() && canUpload) {
            listProof.clear()
            binding.llPhotoContent.removeAllViews()
            listProof.addAll(list)
            listProof.forEach {
                addImageView(it)
            }
            binding.llUploadPhoto.isGone = !canUpload
        } else {
            binding.tvEmptyImage.isGone = canUpload
        }
    }

    private fun addImageView(data: MechanicTaskProofModel) {
        val view = CustomIncidentImageItem(context)
        view.setEditable(canUpload)
        view.rootView.setOnClickListener {
            fragmentManager?.let {
                PhotoDetailDialogFragment.newInstance(data.photoLink).show(it, "")
            }
        }
        val onEdit = {
            toast(context, "Edit")
        }
        val onDelete = {
            onDeleteAction?.invoke(data)
            binding.llPhotoContent.removeView(view)
            binding.llUploadPhoto.isGone = canUpload.not()
        }
        view.initView(data.photoLink, onEdit, onDelete, parentView!!)
        binding.llPhotoContent.addView(view)
        if (listProof.size==maxPhotos || canUpload.not()) binding.llUploadPhoto.isGone = true
    }
}