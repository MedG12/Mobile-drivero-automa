package com.automa.data.qr

import com.automa.data.common.Mapper
import com.automa.data.common.mapToResult
import com.automa.data.qr.mapper.CheckQrPairingMapper
import com.automa.data.qr.remote.QrApiClient
import com.automa.domain.common.ResultWrapper
import com.automa.domain.qr.QrRepository
import com.automa.domain.qr.model.CheckQrPairingModel

class QrRepositoryImpl(
    private val api: QrApiClient
): QrRepository {
    override suspend fun checkQr(token: String): ResultWrapper<Boolean> {
        return try {
            api.checkQr(token).mapToResult(object : Mapper<Boolean, Boolean> {
                override fun mapFromResponse(response: Boolean): Boolean {
                    return response
                }
            })
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun checkQrMechanicMaintenance(
        token: String,
        idScheduledMaintenance: Int
    ): ResultWrapper<List<CheckQrPairingModel>> {
        return try {
            api.checkQrMechanicMaintenance(token, idScheduledMaintenance).mapToResult(CheckQrPairingMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun checkQrFleet(token: String): ResultWrapper<List<CheckQrPairingModel>> {
        return try {
            api.checkFleet(token).mapToResult(CheckQrPairingMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun checkQrFleetPairing(
        idDriver: Int,
        idFleet: Int
    ): ResultWrapper<List<CheckQrPairingModel>> {
        return try {
            api.fleetPairing(idDriver, idFleet).mapToResult(CheckQrPairingMapper())
        } catch (e: Exception) {
            ResultWrapper.fail(e.localizedMessage ?: "Error")
        }
    }
}