package com.automa.ui.mechanic.service_request

import android.content.Intent
import android.os.Bundle
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityServiceRequestBinding
import com.automa.ui.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ServiceRequestActivity : BaseActivity() {
    private lateinit var binding: ActivityServiceRequestBinding

    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initValidation()
        initActions()
    }

    private fun initValidation() {
        binding.tilServiceNumber.onTextValidation(binding.edtServiceNumber)
        binding.tilActivitySummary.onTextValidation(binding.edtActivitySummary)
        binding.tilDescription.onTextValidation(binding.edtDescription)
        binding.tilWorkshop.onTextValidation(binding.edtWorkshop)
        binding.tilDateTime.onTextValidation(binding.edtDateTime)
        binding.tilFleetPlate.onTextValidation(binding.edtFleetPlate)
        binding.tilOdometer.onTextValidation(binding.edtOdometer)
    }

    private fun initActions() {
        val dpd = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val tpd = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        binding.edtDateTime.setOnClickListener {
            dpd.show(supportFragmentManager, "")
        }

        val listWorkshop = mutableListOf<String>()
        for (i in 1..100) {
            listWorkshop.add("Workshop $i")
        }
        binding.edtWorkshop.setItems(listWorkshop)
        binding.edtWorkshop.setOnClickListener {
            binding.edtWorkshop.showDropDown()
        }
        binding.edtWorkshop.setOnItemClickListener { _, _, position, _ ->

        }

        dpd.addOnPositiveButtonClickListener {
            selectedDate = DateUtils.formatDate(Date(it), "dd/MM/yyyy")
            tpd.show(supportFragmentManager, "")
        }

        tpd.addOnPositiveButtonClickListener {
            val newHour: String = if(tpd.hour < 10) {
                String.format("%02d", tpd.hour)
            }else tpd.hour.toString()

            val newMinute: String = if(tpd.minute < 10) {
                String.format("%02d", tpd.minute)
            }else tpd.minute.toString()

            selectedTime = String.format("$newHour:$newMinute")

            binding.edtDateTime.setText(String.format("$selectedDate $selectedTime"))
        }

        binding.toolbarServiceRequest.setOnNavigationClickListener { finish() }

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSubmit.setOnClickListener {
            binding.tilServiceNumber.checkIsNotEmpty(binding.edtServiceNumber)
            binding.tilActivitySummary.checkIsNotEmpty(binding.edtActivitySummary)
            binding.tilDescription.checkIsNotEmpty(binding.edtDescription)
            binding.tilWorkshop.checkIsNotEmpty(binding.edtWorkshop)
            binding.tilDateTime.checkIsNotEmpty(binding.edtDateTime)
            binding.tilFleetPlate.checkIsNotEmpty(binding.edtFleetPlate)
            binding.tilOdometer.checkIsNotEmpty(binding.edtOdometer)

            when {
                binding.tilServiceNumber.error != null -> binding.edtServiceNumber.requestFocus()
                binding.tilActivitySummary.error != null -> binding.edtActivitySummary.requestFocus()
                binding.tilDescription.error != null -> binding.edtDescription.requestFocus()
                binding.tilWorkshop.error != null -> binding.edtWorkshop.requestFocus()
                binding.tilDateTime.error != null -> binding.edtDateTime.requestFocus()
                binding.tilFleetPlate.error != null -> binding.edtFleetPlate.requestFocus()
                binding.tilOdometer.error != null -> binding.edtOdometer.requestFocus()
                else -> {
                    DialogUtils.showDialogInfoWithImage(this, R.drawable.illustration_submit_service_request,
                    getString(R.string.title_create_service_schedule), getString(R.string.messaeg_create_service_schedule),
                    positiveButton = Pair(getString(R.string.button_continue)) {
                        startActivity(Intent(this@ServiceRequestActivity, ServiceRequestAddDetailsActivity::class.java))
                    }, negativeButton = Pair(getString(R.string.cancel)) {

                    })
                }
            }
        }
    }
}