package com.automa.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.automa.datastore.user_data.CurrentDeliveryOrder
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.ui.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun isLoggedIn(userDataModel: UserDataModel): Boolean {
    return userDataModel.token.isNotEmpty()
}

fun String.ifDefaultData(action: (String)->Unit) {
    if (this == "-") action.invoke(this)
}

fun String.ifNotDefaultData(action: (String) -> Unit) {
    if (this != "-") action.invoke(this)
}

fun String.isNotDefaultData(): Boolean {
    return this != "-"
}

fun Int.isNotDefaultData(): Boolean {
    return this != -1
}

fun Double.isNotDefaultData(): Boolean {
    return this != 0.0
}

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun DeliveryOrderModel.mapToLocal(): CurrentDeliveryOrder {
    return CurrentDeliveryOrder(
        id = this.id,
        idCompany = this.idCompany,
        companyName = this.companyName,
        companyPhone = this.companyPhone,
        deliveryOrderNumber = this.deliveryOrderNumber,
        deliveryOrderDesc = this.deliveryOrderDesc,
        idFleet = this.idFleet,
        fleetPlate = this.fleetPlate,
        restInterval = this.restInterval,
        intervalBetweenRest = this.intervalBetweenRest,
        idDriver = this.idDriver,
        driverName = this.driverName,
        driverImageLink = this.driverImageLink,
        driverPhone = this.driverPhone,
        idDriverAssistant = this.idDriverAssistant,
        driverAssistantImageLink = this.driverAssistantImageLink,
        idSecurity = this.idSecurity,
        securityName = this.securityName,
        driverAssistantName = this.driverAssistantName,
        assignedDate = this.assignedDate,
        endAssignedDate = this.endAssignedDate,
        closeStatus = this.closeStatus,
        reportLink = this.reportLink,
        checkSheetDone = false
    )
}

fun AutoCompleteTextView.setItems(
    items: MutableList<String>,
    prefix: String = "",
    layoutId: Int = R.layout.item_spinner
) {
    val spinnerItems = mutableListOf<String>()

    items.forEachIndexed { _, item ->
        spinnerItems.add(String.format("%s %s", prefix, item))
    }

    context?.let { context ->
        val adapter = ArrayAdapter(
            context,
            layoutId,
            spinnerItems
        )
        this.setAdapter(adapter)
    }
}

fun copyToClipboard(context: Context, text: String) {
    try {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun formatNumberWithThousandSeparator(number: Double): String {
    val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
    formatSymbols.groupingSeparator = '.' // Set '.' as thousand separator
    formatSymbols.decimalSeparator = ',' // Set ',' as decimal separator

    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    if (numberFormat is DecimalFormat) {
        numberFormat.decimalFormatSymbols = formatSymbols
    }

    return numberFormat.format(number)
}