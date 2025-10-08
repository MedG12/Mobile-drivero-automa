package com.automa.data.account

import com.automa.data.account.mapper.CertificationMechanicMapper
import com.automa.data.account.mapper.DriverSettingsMapper
import com.automa.data.account.mapper.ProfileDriverMapper
import com.automa.data.account.mapper.ProfileMechanicMapper
import com.automa.data.account.remote.AccountApiClient
import com.automa.data.common.Mapper
import com.automa.data.common.mapToResult
import com.automa.domain.account.AccountRepository
import com.automa.domain.account.model.CertificationMechanicModel
import com.automa.domain.account.model.ProfileDriverModel
import com.automa.domain.account.model.ProfileMechanicModel
import com.automa.domain.account.model.DriverSettingsModel
import com.automa.domain.common.ResultWrapper

class AccountRepositoryImpl(private val api: AccountApiClient): AccountRepository {
    override suspend fun getMechanicProfile(id: Int): ResultWrapper<List<ProfileMechanicModel>> {
        return try {
            api.getMechanicProfile(id).mapToResult(ProfileMechanicMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getDriverProfile(id: Int): ResultWrapper<List<ProfileDriverModel>> {
        return try {
            api.getDriverProfile(id).mapToResult(ProfileDriverMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMechanicCertifications(idMechanic: Int): ResultWrapper<List<CertificationMechanicModel>> {
        return try {
            api.getMechanicCertification(idMechanic).mapToResult(CertificationMechanicMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun inputMechanicCertification(
        idMechanic: Int,
        name: String,
        desc: String,
        certDate: String,
        expDate: String,
        level: String
    ): ResultWrapper<String> {
        return try {
            api.inputMechanicCertification(idMechanic, name, desc, certDate, expDate, level).mapToResult(object : Mapper<HashMap<String, Any>, String> {
                override fun mapFromResponse(response: HashMap<String, Any>): String {
                    return try {
                        response["id"] as String
                    } catch (e: Exception) {
                        ""
                    }
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun updateMechanicCertification(
        id: Int,
        idMechanic: Int,
        name: String,
        desc: String,
        certDate: String,
        expDate: String,
        level: String
    ): ResultWrapper<String> {
        return try {
            api.updateMechanicCertification(id, idMechanic, name, desc, certDate, expDate, level).mapToResult(object : Mapper<String, String> {
                override fun mapFromResponse(response: String): String {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun deleteMechanicCertification(id: Int): ResultWrapper<String> {
        return try {
            api.deleteMechanicCertification(id).mapToResult(object : Mapper<String, String> {
                override fun mapFromResponse(response: String): String {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getDriverSettings(): ResultWrapper<List<DriverSettingsModel>> {
        return try {
            api.getDriverSettings().mapToResult(DriverSettingsMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}