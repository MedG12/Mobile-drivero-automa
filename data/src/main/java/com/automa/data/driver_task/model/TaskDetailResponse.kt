package com.automa.data.driver_task.model


import com.google.gson.annotations.SerializedName

data class TaskDetailResponse(
    @SerializedName("assist_vac_detail") val assistVacDetail: List<Any?>? = null,
    @SerializedName("checkin") val checkin: List<CheckInResponse?>? = null,
    @SerializedName("checkinresult") val checkinresult: String? = null,
    @SerializedName("delay_time") val delayTime: String? = null,
    @SerializedName("distance") val distance: Int? = null,
    @SerializedName("distance_all") val distanceAll: Int? = null,
    @SerializedName("distance_est") val distanceEst: Int? = null,
    @SerializedName("do_details") val doDetails: List<DoDetailResponse?>? = null,
    @SerializedName("driver_vac_detail") val driverVacDetail: List<Any?>? = null,
    @SerializedName("fleet_status") val fleetStatus: FleetStatusResponse? = null,
    @SerializedName("loadingTime") val loadingTime: Int? = null,
    @SerializedName("location_list") val locationList: List<TaskLocationResponse?>? = null,
    @SerializedName("overview_polyline") val overviewPolyline: String? = null
)

data class CheckInResponse(
    @SerializedName("idCheckIn") val idCheckIn: String? = null,
    @SerializedName("idCheckOut") val idCheckOut: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("loadingTime") val loadingTime: String? = null,
    @SerializedName("location") val location: List<TaskLocationResponse?>? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("meterToCheckIn") val meterToCheckIn: String? = null,
    @SerializedName("share") val share: String? = null,
    @SerializedName("timeCheckIn") val timeCheckIn: String? = null,
    @SerializedName("timeCheckOut") val timeCheckOut: String? = null,
    @SerializedName("work_order_from") val workOrderFrom: WorkOrderFromResponse? = null
)

data class TaskLocationResponse(
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lng") val lng: Double? = null
)

data class WorkOrderFromResponse(
    @SerializedName("approval_time") val approvalTime: String? = null,
    @SerializedName("arrival_time") val arrivalTime: String? = null,
    @SerializedName("break_time") val breakTime: Int? = null,
    @SerializedName("cargo_weight") val cargoWeight: Double? = null,
    @SerializedName("company_delivery") val companyDelivery: String? = null,
    @SerializedName("company_requestor") val companyRequestor: String? = null,
    @SerializedName("created_on") val createdOn: String? = null,
    @SerializedName("customer_approval_browser") val customerApprovalBrowser: String? = null,
    @SerializedName("customer_approval_ip") val customerApprovalIp: String? = null,
    @SerializedName("customer_approval_name") val customerApprovalName: String? = null,
    @SerializedName("customer_approval_remarks") val customerApprovalRemarks: String? = null,
    @SerializedName("customer_approval_time") val customerApprovalTime: String? = null,
    @SerializedName("delivery_id") val deliveryId: Int? = null,
    @SerializedName("dept_lat") val deptLat: Double? = null,
    @SerializedName("dept_lng") val deptLng: Double? = null,
    @SerializedName("dept_time") val deptTime: String? = null,
    @SerializedName("distance") val distance: Double? = null,
    @SerializedName("est_arrival_time") val estArrivalTime: String? = null,
    @SerializedName("est_carbon") val estCarbon: Double? = null,
    @SerializedName("est_cost") val estCost: Double? = null,
    @SerializedName("est_departure_time") val estDepartureTime: String? = null,
    @SerializedName("est_stop_time") val estStopTime: Int? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("id_outbound_stocker") val idOutboundStocker: String? = null,
    @SerializedName("id_request") val idRequest: String? = null,
    @SerializedName("id_security") val idSecurity: String? = null,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("link_3pl_stocker") val link3plStocker: String? = null,
    @SerializedName("lng") val lng: Double? = null,
    @SerializedName("loc_name") val locName: String? = null,
    @SerializedName("mc_address") val mcAddress: String? = null,
    @SerializedName("mc_lat") val mcLat: Double? = null,
    @SerializedName("mc_lng") val mcLng: Double? = null,
    @SerializedName("mc_name") val mcName: String? = null,
    @SerializedName("mc_phone") val mcPhone: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("notes_for_driver") val notesForDriver: String? = null,
    @SerializedName("pic_name") val picName: String? = null,
    @SerializedName("pic_phone") val picPhone: String? = null,
    @SerializedName("real_distance") val realDistance: String? = null,
    @SerializedName("sp_address") val spAddress: String? = null,
    @SerializedName("sp_lat") val spLat: Double? = null,
    @SerializedName("sp_lng") val spLng: Double? = null,
    @SerializedName("sp_name") val spName: String? = null,
    @SerializedName("sp_phone") val spPhone: String? = null,
    @SerializedName("status_customer_approval") val statusCustomerApproval: String? = null,
    @SerializedName("status_customer_approval_detail") val statusCustomerApprovalDetail: String? = null,
    @SerializedName("status_delivery") val statusDelivery: String? = null,
    @SerializedName("status_delivery_detail") val statusDeliveryDetail: String? = null,
    @SerializedName("wo_address") val woAddress: String? = null,
    @SerializedName("wo_desc") val woDesc: String? = null,
    @SerializedName("wo_destination_name") val woDestinationName: String? = null,
    @SerializedName("wo_number") val woNumber: String? = null,
    @SerializedName("wo_seq") val woSeq: Int? = null
)

data class DoDetailResponse(
    @SerializedName("assigned_do_date") val assignedDoDate: String? = null,
    @SerializedName("avoid_toll") val avoidToll: Int? = null,
    @SerializedName("close_status") val closeStatus: Int? = null,
    @SerializedName("created_on") val createdOn: String? = null,
    @SerializedName("do_desc") val doDesc: String? = null,
    @SerializedName("do_number") val doNumber: String? = null,
    @SerializedName("draft_value") val draftValue: Int? = null,
    @SerializedName("driver_assist_photo_link") val driverAssistPhotoLink: String? = null,
    @SerializedName("driver_assistant_name") val driverAssistantName: String? = null,
    @SerializedName("driver_name") val driverName: String? = null,
    @SerializedName("driver_photo_link") val driverPhotoLink: String? = null,
    @SerializedName("end_assigned_do_date") val endAssignedDoDate: String? = null,
    @SerializedName("fleet_plate") val fleetPlate: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("id_company") val idCompany: Int? = null,
    @SerializedName("id_driver") val idDriver: Int? = null,
    @SerializedName("id_driver_assistant") val idDriverAssistant: Int? = null,
    @SerializedName("id_fleet") val idFleet: Int? = null,
    @SerializedName("optimize") val optimize: Int? = null,
    @SerializedName("overview_polyline") val overviewPolyline: String? = null,
    @SerializedName("total_est_carbon") val totalEstCarbon: Double? = null,
    @SerializedName("total_est_delivery_cost") val totalEstDeliveryCost: Double? = null,
    @SerializedName("total_tkm") val totalTkm: Double? = null
)

data class FleetStatusResponse(
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lon") val lon: Double? = null
)