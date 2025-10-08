package com.automa.ui.mechanic.breakdown_report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.domain.breakdown_report.model.BreakdownCategoryModel
import com.automa.domain.breakdown_report.model.BreakdownSubCategoryModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.R
import com.automa.ui.custom_component.check_box.CheckBoxDataModel
import com.automa.ui.custom_component.check_box.CheckBoxItem
import com.automa.ui.databinding.FragmentBottomsheetBreakdownCategoryBinding
import com.automa.ui.mechanic.breakdown_report.adapter.BreakdownCategoryAdapter
import com.automa.ui.utils.setItems
import com.automa.ui.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakdownCategoryBottomSheetFragment(
    private val listener: (selectedSubCategory: BreakdownSubCategoryModel)->Unit
): BottomSheetDialogFragment() {
    private var _binding: FragmentBottomsheetBreakdownCategoryBinding ?= null
    private val binding get() = _binding!!

    private val breakdownReportViewModel: BreakdownReportViewModel by viewModels()

    private val breakdownCategoryAdapter = BreakdownCategoryAdapter()

    private var listSubCategory = listOf<BreakdownSubCategoryModel>()
    private var listCategory = listOf<BreakdownCategoryModel>()
    private var selectedListSubCategory: BreakdownSubCategoryModel ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomsheetBreakdownCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRv()
        initObserver()
        initActions()
        breakdownReportViewModel.getSubCategory(0)
    }

    private fun initRv() {
        binding.rvCheckBoxes.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = breakdownCategoryAdapter
        }
    }

    private fun initActions() {
        binding.edtCategory.setOnClickListener {
            binding.edtCategory.showDropDown()
        }
        binding.edtCategory.setOnItemClickListener { _, _, i, _ ->
//            selectedCategory = listCategory[i]
//            breakdownReportViewModel.getSubCategory(selectedCategory?.idCatagoricalSub ?: 0)
        }

        binding.btnSubmit.setOnClickListener {
            selectedListSubCategory = breakdownCategoryAdapter.getSelectedCategory()
            if (selectedListSubCategory!=null) {
                listener.invoke(selectedListSubCategory!!)
                dismiss()
            } else {
                toast(requireActivity(), "Silakan pilih kategori")
            }
//            binding.llCheckBoxes.children.forEach {
//                if (it is CheckBoxItem) {
//                    val value = it.getValue()
//                    if (value.checked) {
//                        val find = listSubCategory.find { item -> item.name == value.name }
//                        if (find != null) selectedListSubCategory.add(find)
//                    }
//                }
//            }
//            if (selectedCategory != null && selectedListSubCategory != null) {
//                listener.invoke(selectedCategory!!., selectedListSubCategory)
//                dismiss()
//            } else {
//                toast(requireActivity(), "Silakan pilih kategori")
//            }
        }

        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

    private fun initObserver() {
        breakdownReportViewModel.category.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    listCategory = success.data
//                    setCategoryData()
                }, onFailure = { error ->
                    toast(requireActivity(), error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }

        breakdownReportViewModel.subCategory.observe(viewLifecycleOwner) {
            it.handleResult(
                onSuccess = { success ->
                    listSubCategory = success.data
                    breakdownCategoryAdapter.setData(success.data)
//                    setCheckBoxSubCategory()
                }, onFailure = { error ->
                    toast(requireActivity(), error.errorData.message ?: "Terjadi Kesalahan")
                }
            )
        }
    }
}