package com.automa.data.driver_task.model

import com.google.gson.annotations.SerializedName

data class MasterDeliveryOrderResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_company")
    val idCompany: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("subcategory")
    val subCategory: List<MasterDeliveryOrderSubItemResponse>? = null
)

data class MasterDeliveryOrderSubItemResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_delivery_order_category")
    val idDeliveryOrderCategory: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("do_list")
    val masterDoList: List<DeliveryOrderResponse>? = null
)

data class MasterDeliveryOrderCategoryResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_company")
    val idCompany: Int? = null,
    @SerializedName("name")
    val name: String? = null
)

data class MasterDeliveryOrderSubCategoryResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_delivery_order_category")
    val idDeliveryOrderCategory: Int? = null,
    @SerializedName("name")
    val name: String? = null
)