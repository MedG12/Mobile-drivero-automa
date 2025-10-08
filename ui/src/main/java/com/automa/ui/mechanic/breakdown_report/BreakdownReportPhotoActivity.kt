package com.automa.ui.mechanic.breakdown_report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.automa.domain.breakdown_report.model.BreakdownReportItemModel
import com.automa.domain.breakdown_report.model.BreakdownReportPhotoModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.base.BaseActivity
import com.automa.ui.custom_component.AccordionPod
import com.automa.ui.databinding.ActivityBreakdownReportPhotoBinding
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakdownReportPhotoActivity : BaseActivity() {
    companion object {
        private const val DATA = "DATA"
        fun newIntent(context: Context, reportData: BreakdownReportItemModel): Intent {
            val intent = Intent(context, BreakdownReportPhotoActivity::class.java)
            intent.putExtra(DATA, reportData)
            return intent
        }
    }

    private lateinit var binding: ActivityBreakdownReportPhotoBinding
    private lateinit var reportData: BreakdownReportItemModel
    private val viewModel: BreakdownReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreakdownReportPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarViewPictures.setOnNavigationClickListener { finish() }

        initObserver()
        reportData = intent.getParcelableExtra(DATA)!!
        binding.toolbarViewPictures.setToolbarText(String.format("${reportData.name} (${reportData.regNumber})"))
        viewModel.getImage(reportData.id)
    }

    private fun initObserver() {
        viewModel.getPhoto.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    addAccordion(success.data)
                }, onFailure = { error ->
                    toast(this, error.errorData.toString())
                }
            )
        }
    }

    private fun addAccordion(data: List<BreakdownReportPhotoModel>) {
        binding.llListPod.removeAllViews()
        if (data.isNotEmpty()) {
            val imageToShow = if (data.size>2) data.takeLast(2) else data
            imageToShow.forEachIndexed { i, item ->
                val view = AccordionPod(this)
                view.setData(String.format("#${i+1}"), reportData.regNumber, item)
                binding.llListPod.addView(view)
            }
        }
    }
}