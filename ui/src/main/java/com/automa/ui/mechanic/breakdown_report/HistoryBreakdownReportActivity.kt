package com.automa.ui.mechanic.breakdown_report

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityHistoryBreakdownReportBinding
import com.automa.ui.mechanic.breakdown_report.adapter.HistoryBreakdownAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryBreakdownReportActivity : BaseActivity() {
    private lateinit var binding: ActivityHistoryBreakdownReportBinding
    private val historyBreakdownAdapter = HistoryBreakdownAdapter {
        startActivity(BreakdownReportPhotoActivity.newIntent(this@HistoryBreakdownReportActivity, it))
    }

    private val viewModel: BreakdownReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBreakdownReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarHistoryBreakdown.setOnNavigationClickListener { finish() }

        binding.llAddBreakdownReport.setOnClickListener {
            startActivity(Intent(this, BreakdownReportActivity::class.java))
        }
        initRv()
        initObserver()
        viewModel.getBreakdownReports()
    }

    private fun initRv() {
        binding.rvHistoryBreakdown.apply {
            layoutManager = LinearLayoutManager(this@HistoryBreakdownReportActivity, LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = historyBreakdownAdapter
        }
    }

    private fun initObserver() {
        viewModel.reports.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    historyBreakdownAdapter.setData(success.data.data.sortedByDescending { item -> item.createdOn })
                }, onFailure = { error ->
                    viewModel.getBreakdownReports()
                }
            )
        }
    }
}