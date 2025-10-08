package com.automa.ui.shared.photo_detail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.automa.ui.R
import com.automa.ui.databinding.FragmentPhotoDetailDialogBinding
import com.bumptech.glide.Glide

class PhotoDetailDialogFragment : DialogFragment() {
    private var _binding : FragmentPhotoDetailDialogBinding?= null
    private val binding get() = _binding!!
    private lateinit var photoUrl: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                dialog?.dismiss()
            }
        }.apply<Dialog> {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog!=null){
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPhotoDetailDialogBinding.inflate(inflater, container, false)

        arguments?.let {
            photoUrl = it.getString("URL").toString()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::photoUrl.isInitialized){
            Glide.with(requireContext())
                .load(photoUrl)
                .placeholder(R.drawable.ic_user_default)
                .error(R.drawable.ic_user_default)
                .into(binding.ivPhoto)
        }

        binding.btnBack.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(url: String) =
            PhotoDetailDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("URL", url)
                }
            }
    }
}