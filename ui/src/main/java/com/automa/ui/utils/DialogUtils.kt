package com.automa.ui.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.domain.driver_task.model.WorkOrderModel
import com.automa.domain.live_monitoring.model.LiveMonitoringModel
import com.automa.domain.mechanic_task.model.MechanicTaskModel
import com.automa.ui.R
import com.automa.ui.databinding.*
import com.automa.ui.driver.home.LocationDetailActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.lang.Exception
import java.util.*
import kotlin.math.roundToInt

object DialogUtils {
    fun showDuplicateDeliveryOrderDialog(
        context: Context,
        supportFragmentManager: FragmentManager,
        deliveryOrder: DeliveryOrderModel,
        workOrder: WorkOrderModel,
        isDuplicateMaster: Boolean,
        onAccept: (idDO: Int, assignedDate: String, estDepartureTime: String, endDate: String)->Unit
    ) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetStyle)
        val binding = DialogDuplicateDeliveryOrderBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        var selectedDate = ""
        var endDate = ""
        dialog.setOnShowListener {
            val bottomSheet: View? = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.llFleetPlate.isGone = isDuplicateMaster
        binding.llDriverAssistant.isGone = isDuplicateMaster
        binding.tvDONumber.text = deliveryOrder.deliveryOrderNumber
        binding.tvDODesc.text = deliveryOrder.deliveryOrderDesc
        binding.tvDriverAssistant.text = deliveryOrder.driverAssistantName
        binding.tvFleetPlateNumber.text = deliveryOrder.fleetPlate

        binding.btnBack.setOnClickListener { dialog.dismiss() }
        binding.btnAccept.setOnClickListener {
            if (selectedDate.isNotEmpty() && endDate.isNotEmpty()) {
                onAccept.invoke(deliveryOrder.id, selectedDate, selectedDate, endDate)
                dialog.dismiss()
            } else {
                toast(context, "Pilih waktu terlebih dahulu")
            }
        }
        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnViewMap.setOnClickListener {
            context.startActivity(LocationDetailActivity.newIntent(context, workOrder))
        }

        val tpd = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
        val date = DateUtils.formatDate(Date(), "dd/MM/yyyy")
        tpd.addOnPositiveButtonClickListener {
            val newHour: String = if(tpd.hour < 10) {
                String.format("%02d", tpd.hour)
            } else tpd.hour.toString()

            val newMinute: String = if(tpd.minute < 10) {
                String.format("%02d", tpd.minute)
            } else tpd.minute.toString()

            val selectedTime = String.format("$newHour:$newMinute")
            val dateToShow = String.format("$date $selectedTime")
            selectedDate = DateUtils.formatDate(String.format("$date $selectedTime"), "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")
            val parsedSelectedDate = DateUtils.parseDate(selectedDate, "yyyy-MM-dd HH:mm:ss")
            val parsedAssignedDoDate = DateUtils.parseDate(deliveryOrder.assignedDate, "yyyy-MM-dd HH:mm:ss")
            val parsedEndDoDate = DateUtils.parseDate(deliveryOrder.endAssignedDate, "yyyy-MM-dd HH:mm:ss")
            val subtractedDate = parsedEndDoDate - parsedAssignedDoDate
            endDate = DateUtils.formatDate(Date(parsedSelectedDate + subtractedDate), "yyyy-MM-dd HH:mm:ss")
            binding.edtTime.setText(dateToShow)
        }

        binding.edtTime.setOnClickListener { tpd.show(supportFragmentManager, "") }

        workOrder.result.forEach {
            val item = ItemTaskLocationBinding.inflate(LayoutInflater.from(context))
            item.tvLocationName.text = it.locName
            item.tvViewMap.setOnClickListener {
                toast(context, "Lihat Map")
            }
            binding.llLocations.addView(item.root)
        }

        dialog.show()
    }

    fun showWorkOrderDialog(
        context: Context,
        deliveryOrder: DeliveryOrderModel,
        workOrder: WorkOrderModel,
        onAccept: ()->Unit) {

        val dialog = BottomSheetDialog(context, R.style.BottomSheetStyle)
        val binding = DialogWorkOrderBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener {
            val bottomSheet: View? = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.tvDONumber.text = deliveryOrder.deliveryOrderNumber
        binding.tvDODesc.text = deliveryOrder.deliveryOrderDesc
        binding.tvDriverAssistant.text = deliveryOrder.driverAssistantName
        binding.tvFleetPlateNumber.text = deliveryOrder.fleetPlate

        binding.btnBack.setOnClickListener { dialog.dismiss() }
        binding.btnAccept.setOnClickListener {
            onAccept.invoke()
            dialog.dismiss()
        }
        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnViewMap.setOnClickListener {
            context.startActivity(LocationDetailActivity.newIntent(context, workOrder))
        }

        workOrder.result.forEach {
            val item = ItemTaskLocationBinding.inflate(LayoutInflater.from(context))
            item.tvLocationName.text = it.locName
            item.tvViewMap.setOnClickListener {
                toast(context, "Lihat Map")
            }
            binding.llLocations.addView(item.root)
        }

        dialog.show()
    }

    fun showWhiteAlertDialog(
        context: Context,
        title: String? = null,
        message: String? = null,
        positiveButton: Pair<String, () -> Unit>? = null,
        negativeButton: Pair<String, () -> Unit>? = null
    ) {

        val builder = AlertDialog.Builder(
            ContextThemeWrapper(
                context,
                android.R.style.Theme_Material_Light_Dialog_Alert
            )
        )

        builder.apply {
            if (title != null) setTitle(title)

            if (message != null) setMessage(message)

            if (negativeButton != null) {
                setNegativeButton(
                    negativeButton.first
                ) { _, _ ->
                    negativeButton.second.invoke()
                }
            }

            if (positiveButton != null) {
                setPositiveButton(
                    positiveButton.first
                ) { _, _ ->
                    positiveButton.second.invoke()
                }
            }
            setCancelable(false)
        }

        val dialog = builder.create()
        dialog.show()

        if (negativeButton != null) {
            val buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonNegative.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent))
            buttonNegative.setTextColor(ContextCompat.getColor(context, R.color.primary500))
        }

        if (positiveButton != null) {
            val buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            buttonPositive.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent))
            buttonPositive.setTextColor(ContextCompat.getColor(context, R.color.error500))
        }
    }

    fun showDialogInfoWithImage(
        context: Context,
        @DrawableRes image: Int ?= null,
        title: String?, message: String?,
        positiveButton: Pair<String, ()->Unit>,
        negativeButton: Pair<String, ()->Unit> ?= null) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogInfoBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setLayout(
            ((context.resources.displayMetrics.widthPixels * 0.90).toInt()),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_dialog_rounded))

        binding.btnAction.text = positiveButton.first
        if (image != null) {
            binding.ivDialog.setImageResource(image)
        } else {
            binding.ivDialog.isGone = true
        }
        title?.let {
            binding.tvTitleDialog.text = it
        }
        message?.let {
            binding.tvMessageDialog.text = it
        }

        binding.btnAction.setOnClickListener {
            dialog.dismiss()
            positiveButton.second.invoke()
        }

        if (negativeButton != null) {
            binding.btnNegative.isGone = false
            binding.btnNegative.text = negativeButton.first
            binding.btnNegative.setOnClickListener {
                dialog.dismiss()
                negativeButton.second.invoke()
            }
        }
        dialog.show()
    }

    fun showDialogForgotPassword(context: Context, isSuccess: Boolean, action: ()->Unit) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogForgotPasswordBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setLayout(
            ((context.resources.displayMetrics.widthPixels * 0.90).toInt()),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_dialog_rounded))

        if (isSuccess) {
            binding.tvMessage.text = context.getString(R.string.message_success_forgot_password)
            binding.ivDialog.setImageResource(R.drawable.illustration_success_forgot_password)
            binding.llPhone.isGone = true
        } else {
            binding.tvMessage.text = context.getString(R.string.message_fail_forgot_password)
            binding.ivDialog.setImageResource(R.drawable.illustration_failed_forgot_password)
            binding.llPhone.isGone = false
        }

        binding.btnAction.setOnClickListener {
            action.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showImagePickerDialog(context: Context, cameraAction: ()->Unit, galleryAction: ()->Unit) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogImagePickerBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setLayout(
            ((context.resources.displayMetrics.widthPixels * 0.90).toInt()),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_dialog_rounded))

        binding.llPickFromCamera.setOnClickListener {
            cameraAction.invoke()
            dialog.dismiss()
        }
        binding.llPickFromGallery.setOnClickListener {
            galleryAction.invoke()
            dialog.dismiss()
        }
        binding.llCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    fun showServiceRequestBottomSheet(
        context: Context,
        onAddNew: ()->Unit,
        onViewHistory: ()->Unit,
        onViewStatus: ()->Unit) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetStyle)
        val binding = BottomsheetServiceRequestBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

        binding.ivClose.setOnClickListener { dialog.dismiss() }
        binding.llAddServiceRequest.setOnClickListener {
            onAddNew.invoke()
            dialog.dismiss()
        }
        binding.llServiceHistory.setOnClickListener {
            onViewHistory.invoke()
            dialog.dismiss()
        }
        binding.llServiceStatus.setOnClickListener {
            onViewStatus.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showBreakdownReportBottomSheet(
        context: Context,
        onAddNew: ()->Unit,
        onViewHistory: ()->Unit,
        onViewStatus: ()->Unit) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetStyle)
        val binding = BottomsheetBreakdownReportBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

        binding.ivClose.setOnClickListener { dialog.dismiss() }
        binding.llAddBreakdownReport.setOnClickListener {
            onAddNew.invoke()
            dialog.dismiss()
        }
        binding.llBreakdownReportHistory.setOnClickListener {
            onViewHistory.invoke()
            dialog.dismiss()
        }
        binding.llBreakdownReportStatus.setOnClickListener {
            onViewStatus.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showMechanicAcceptTaskDialog(
        context: Context,
        taskModel: MechanicTaskModel,
        onAccept: ()->Unit) {

        val dialog = BottomSheetDialog(context, R.style.BottomSheetStyle)
        val binding = DialogMechanicTaskBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnShowListener {
            val bottomSheet: View? = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.tvLabelDescription.append(":\t\t")
        binding.tvLabelPlateFleet.append(":\t")
        binding.tvLabelWorkshop.append(":\t")
        binding.tvLabelTime.append(":\t\t\t\t")


        binding.tvMaintenanceNumber.text = taskModel.maintenanceNumber
        binding.tvDescription.text = taskModel.maintenanceDesc
        binding.tvWorkshop.text = taskModel.workshopName
        binding.tvFleetPlateNumber.text = if (taskModel.fleetRegNumber.isNotDefaultData()) taskModel.fleetRegNumber else taskModel.fleetNotes
        binding.tvTime.text = taskModel.scheduledDatetime

        binding.btnBack.setOnClickListener { dialog.dismiss() }
        binding.btnAccept.setOnClickListener {
            onAccept.invoke()
            dialog.dismiss()
        }

        if (taskModel.tasks.isEmpty()) {
            binding.llTasksInfo.isGone = true
        }
        taskModel.tasks.forEach {
            val item = ItemTaskLocationBinding.inflate(LayoutInflater.from(context))
            item.tvLocationName.text = it.taskName
            item.tvViewMap.setOnClickListener {
                toast(context, "Lihat Map")
            }
            binding.llTask.addView(item.root)
        }

        dialog.show()
    }

    fun showLiveMonitoringDetailDialog(
        context: Context,
        itemData: LiveMonitoringModel
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = BottomsheetLiveMonitoringDetailBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            ((context.resources.displayMetrics.widthPixels * 0.90).toInt()),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_dialog_rounded))

        binding.tvFleetId.text = itemData.regNumberWithDoor
        binding.tvObd.text = itemData.obdCode
        binding.tvLastUpdated.text = DateUtils.formatDate(itemData.lastUpdateWithSpeed, DateUtils.DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm")
        //Fuel Level
        if (itemData.fuelLevel.isNotDefaultData() && itemData.fuelTankCapacity.isNotDefaultData()) {
            val currentFuel = (itemData.fuelLevel/100)*itemData.fuelTankCapacity
            binding.tvFuelLevel.text = String.format("$currentFuel L /${itemData.fuelTankCapacity} L")
        } else {
            binding.tvFuelLevel.text = "-"
        }

        //Speed
        if (itemData.spd.isNotDefaultData()) {
            binding.tvSpeed.text = String.format("${itemData.spd} km/h")
        } else {
            binding.tvSpeed.text = "-"
        }

        //Odometer
        if (itemData.odometer.isNotDefaultData()) {
            binding.tvOdometer.text = String.format("${formatNumberWithThousandSeparator(itemData.odometer.div(1000.0))} km")
        } else {
            binding.tvOdometer.text = "-"
        }

        //Status
        if (itemData.wrn.isNotDefaultData() && itemData.spd.isNotDefaultData()) {
            when {
                itemData.wrn == 0 && itemData.spd > 0 -> {
                    binding.tvStatus.text = "Off (Warning!)"
                }
                itemData.wrn == 0 -> {
                    binding.tvStatus.text = "Off"
                }
                itemData.wrn == 1 && itemData.spd > 0 -> {
                    binding.tvStatus.text = "On & Moving"
                }
                itemData.wrn == 1 && itemData.spd == 0 -> {
                    binding.tvStatus.text = "On & Idle"
                }
            }
        } else {
            binding.tvStatus.text = "-"
        }

        //Indicator Color
        if (itemData.lastUpdateWithSpeed.isNotDefaultData()) {
            try {
                val long = DateUtils.parseDate(itemData.lastUpdateWithSpeed, DateUtils.DEFAULT_DATE_FORMAT)
                val currentTimeLong = Date().time
                if (currentTimeLong > long) {
                    val calc : Double = (currentTimeLong - long).toDouble().div(60)
                    when {
                        calc > 0.0 && calc <= 15.0 -> {
                            binding.llChip.isInvisible = false
                            binding.llChip.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.success200))
                            binding.tvChip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.success500))
                            binding.tvChip.text = binding.root.context.getString(R.string.label_fleet_online)
                        }
                        calc > 15.0 && calc <= 60 -> {
                            binding.llChip.isInvisible = false
                            binding.llChip.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.warning200))
                            binding.tvChip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.warning500))
                            binding.tvChip.text = binding.root.context.getString(R.string.label_fleet_online)
                        }
                        calc > 60 -> {
                            binding.llChip.isInvisible = false
                            binding.llChip.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.error200))
                            binding.tvChip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.error500))
                            binding.tvChip.text = binding.root.context.getString(R.string.label_fleet_offline)
                        }
                        else -> {
                            binding.llChip.isInvisible = true
                        }
                    }
                } else {
                    binding.llChip.isInvisible = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.llChip.isInvisible = true
            }
        } else {
            binding.llChip.isInvisible = true
        }

        //Position
        if (itemData.lat.isNotDefaultData() && itemData.lng.isNotDefaultData()) {
            binding.tvPosition.text = String.format("${itemData.lat}, ${itemData.lng}")
            binding.tvOpenMaps.isVisible = true
        } else {
            binding.tvPosition.text = "-"
            binding.tvOpenMaps.isVisible = false
        }

        binding.tvOpenMaps.setOnClickListener {
            val uri = "geo:${itemData.lat},${itemData.lng}?q=${itemData.lat},${itemData.lng}(Fleet ${itemData.regNumberWithDoor})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                toast(context, "Google Maps is not installed")
            }
        }

        dialog.show()
    }

    fun showFullScreenImageDialog(context: Context, imageUrl: String) {
        val dialog = Dialog(context)
        val binding = DialogFullScreenImageBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

        binding.ivClose.setOnClickListener { dialog.dismiss() }
        Glide.with(context)
            .load(imageUrl)
            .into(binding.ivFullScreen)

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.black)

        // Show the dialog
        dialog.show()
    }
}