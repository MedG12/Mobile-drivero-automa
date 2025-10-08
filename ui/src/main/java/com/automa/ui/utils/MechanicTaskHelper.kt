package com.automa.ui.utils

object MechanicTaskHelper {
    private const val STATUS_NEW_ORDER = "New Order"
    private const val STATUS_ACCEPTED_ORDER = "Order Accepted"
    private const val STATUS_ORDER_DETAIL_SET = "Order Detail Approved"
    private const val STATUS_CLIENT_APPROVAL = "Client Approved"
    private const val STATUS_IN_PROGRESS = "Maintenance Can Start"
    private const val STATUS_ORDER_FINISH = "Maintenance Finish"
    private const val STATUS_MAINTENANCE_MANAGER_APPROVAL = "Maintenance Finish Approved"
    private const val STATUS_NEED_PAYMENT = "Waiting Client Approval"
    private const val STATUS_PAID = "Approved by Client"

    fun getStatusName(status: Int): String {
        return when (status) {
            1 -> STATUS_NEW_ORDER
            2 -> STATUS_ACCEPTED_ORDER
            3 -> STATUS_ORDER_DETAIL_SET
            4 -> STATUS_CLIENT_APPROVAL
            5 -> STATUS_IN_PROGRESS
            6 -> STATUS_ORDER_FINISH
            7 -> STATUS_MAINTENANCE_MANAGER_APPROVAL
            8 -> STATUS_NEED_PAYMENT
            9 -> STATUS_PAID
            else -> ""
        }
    }
}