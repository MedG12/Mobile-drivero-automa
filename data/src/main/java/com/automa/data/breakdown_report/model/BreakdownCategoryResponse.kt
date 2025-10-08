package com.automa.data.breakdown_report.model


import com.google.gson.annotations.SerializedName

data class BreakdownCategoryResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("id_company")
    val idCompany: Int? = null,
    @SerializedName("id_catagorical_sub")
    val idCatagoricalSub: Int? = null,
    @SerializedName("main_catagorical")
    val mainCatagorical: String? = null,
    @SerializedName("sub_catagorical_name")
    val subCatagoricalName: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("desc")
    val desc: String? = null,
    @SerializedName("id_fleet")
    val idFleet: Int? = null,
    @SerializedName("reg_number")
    val regNumber: String? = null,
    @SerializedName("id_user")
    val idUser: Int? = null,
    @SerializedName("user_first_name")
    val userFirstName: String? = null,
    @SerializedName("id_check_sheet_details")
    val idCheckSheetDetails: Any? = null,
    @SerializedName("id_status")
    val idStatus: Int? = null,
    @SerializedName("created_on")
    val createdOn: String? = null
)