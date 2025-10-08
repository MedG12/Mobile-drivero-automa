package com.automa.domain.account

import com.automa.domain.account.model.CertificationMechanicModel
import com.automa.domain.account.model.ProfileDriverModel
import com.automa.domain.account.model.ProfileMechanicModel
import com.automa.domain.account.model.DriverSettingsModel
import com.automa.domain.common.ResultWrapper

interface AccountRepository {
    suspend fun getMechanicProfile(id: Int): ResultWrapper<List<ProfileMechanicModel>>
    suspend fun getDriverProfile(id: Int): ResultWrapper<List<ProfileDriverModel>>
    suspend fun getMechanicCertifications(idMechanic: Int): ResultWrapper<List<CertificationMechanicModel>>
    suspend fun inputMechanicCertification(idMechanic: Int, name: String, desc: String, certDate: String, expDate: String, level: String): ResultWrapper<String>
    suspend fun updateMechanicCertification(id: Int, idMechanic: Int, name: String, desc: String, certDate: String, expDate: String, level: String): ResultWrapper<String>
    suspend fun deleteMechanicCertification(id: Int): ResultWrapper<String>

    suspend fun getDriverSettings(): ResultWrapper<List<DriverSettingsModel>>
}