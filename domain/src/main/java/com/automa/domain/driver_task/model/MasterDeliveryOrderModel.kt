package com.automa.domain.driver_task.model

data class MasterDeliveryOrderItemModel(
    val id: Int,
    val idCompany: Int,
    val name: String,
    val createdAt: String,
    val subCategory: List<MasterDeliveryOrderSubItemModel>
)

data class MasterDeliveryOrderSubItemModel(
    val id: Int,
    val idDeliveryOrderCategory: Int,
    val name: String,
    val masterDoList: List<DeliveryOrderModel>
)

data class MasterDeliveryOrderCategoryModel(
    val id: Int,
    val idCompany: Int,
    val name: String
)

data class MasterDeliveryOrderSubCategoryModel(
    val id: Int,
    val idDeliveryOrderCategory: Int,
    val name: String
)
