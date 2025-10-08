package com.automa.domain.qr

import com.automa.domain.common.ResultWrapper
import com.automa.domain.qr.model.CheckQrPairingModel

interface QrRepository {
    suspend fun checkQr(token: String): ResultWrapper<Boolean>
    suspend fun checkQrMechanicMaintenance(token: String, idScheduledMaintenance: Int): ResultWrapper<List<CheckQrPairingModel>>
    suspend fun checkQrFleet(token: String): ResultWrapper<List<CheckQrPairingModel>>
    suspend fun checkQrFleetPairing(idDriver: Int, idFleet: Int): ResultWrapper<List<CheckQrPairingModel>>
}