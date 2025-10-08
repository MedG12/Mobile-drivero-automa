package com.automa.ui.mechanic.service_request

import android.os.Bundle
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityServiceRequestAddDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceRequestAddDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityServiceRequestAddDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceRequestAddDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llAddServiceDetail.setOnClickListener {
            binding.customAddServiceDetail.addServiceDetail()
        }
        binding.customAddServiceDetail.addServiceDetail()
    }
}