package com.automa.datastore.user_data

import kotlinx.serialization.Serializable

@Serializable
data class UserDataModel(
    val token: String = "",
    val roleType: UserRoleType = UserRoleType.NONE,
    val loginData: LoginData ?= null,
    val driverProfile: ProfileDriverData ?= null,
    val mechanicProfile: ProfileMechanicData ?= null,
    val currentDeliveryOrder: CurrentDeliveryOrder ?= null,
    val refreshHome: Boolean ?= false,
    val listNotification: List<NotificationModel> = listOf()
)

@Serializable
data class CurrentDeliveryOrder(
    val id: Int,
    val idCompany: Int,
    val companyName: String,
    val companyPhone: String,
    val deliveryOrderNumber: String,
    val deliveryOrderDesc: String,
    val idFleet: Int,
    val fleetPlate: String,
    val restInterval: Int,
    val intervalBetweenRest: Int,
    val idDriver: Int,
    val driverName: String,
    val driverImageLink: String,
    val driverPhone: String,
    val idDriverAssistant: Int,
    val driverAssistantImageLink: String,
    val idSecurity: Int,
    val securityName: String,
    val driverAssistantName: String,
    val assignedDate: String,
    val endAssignedDate: String,
    val closeStatus: Int,
    val reportLink: String,
    var checkSheetDone: Boolean = false
)

@Serializable
data class LoginData(
    val userId: Int = -1,
    val name: String = "",
    val email: String = "",
    val emailVerified: Boolean = false,
    val idCompany: Int = -1,
    val companyName: String = "",
    val companyPhone: String = "",
    val idRoles: Int = -1,
    val roleName: String = "",
    val subsData: LoginSubsData = LoginSubsData()
)

@Serializable
data class LoginSubsData(
    val idPlan: Int = -1,
    val planName: String = "",
    val totalFleet: Int = -1,
    val userLevel: String = "",
    val userAmount: Int = -1,
    val managerApproval: Int = -1,
    val securityApproval: Int = -1,
    val totalDriver: Int = -1,
    val totalDriverAss: Int = -1,
    val customer: Int= -1,
    val fleetHistory: Int = -1,
    val advanceSchedule: Int = -1,
    val ddor: Int = -1,
    val utilityReport: Int = -1,
    val disconnectedReport: Int = -1,
    val sharedTrack: Int = -1
)

@Serializable
data class ProfileMechanicData(
    val id: Int = -1,
    val nik: String = "",
    val idCompany: Int = -1,
    val userId: Int = -1,
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val joinDate: String = "",
    val leaveDate: String = "",
    val address: String = "",
    val isActive: Int = -1,
    val unactiveReason: String = "",
    val photoLink: String = "",
    val whatsappNumber: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val experienceRemark: String = "",
    val idMechanicPosition: Int = -1,
    val mechanicPositionName: String =""
)

@Serializable
data class ProfileDriverData(
    val id: Int = -1,
    val idCompany: Int = -1,
    val idImageLog: Int = -1,
    val linkImage: String = "",
    val descImage: String = "",
    val idImageLogLic: String = "",
    val linkImageLic: String = "",
    val descImageLic: String = "",
    val name: String = "",
    val telp: String = "",
    val ktp: String = "",
    val birthDate: String = "",
    val idDriverLic: Int = -1,
    val licType: String = "",
    val licNumber: String = "",
    val expDate: String = "",
    val userId: Int = -1,
    val createdOn: String ?= "",
    val pairedFleet: PairedFleet ?= null,
    val statusCopyDo: Boolean = false
)

@Serializable
data class PairedFleet(
    val id: Int,
    val idCompany: Int,
    val company: String,
    val idCarGeneralType: Int,
    val carGeneralType: String,
    val idCarBrands: Int,
    val carBrands: String,
    val idCarType: Int,
    val carType: String,
    val regNumber: String,
    val regNumberWithDoor: String,
    val regYear: Int,
    val manufactureYear: Int,
    val cylCap: Double,
    val vehicleIdNumber: String,
    val engineNumber: String,
    val active: Int
)

@Serializable
data class NotificationModel(
    val type: NotificationType,
    val fields: List<String>
)

enum class UserRoleType(name: String) {
    DRIVER("driver"),
    MECHANIC("mechanic"),
    HEAD_MECHANIC("head_mechanic"),
    LIVE_MONITORING("live_monitoring"),
    NONE("")
}

enum class NotificationType {
    DRIVER_TASK,
    MECHANIC_TASK_DETAIL,
    MECHANIC_TASK_START
}
