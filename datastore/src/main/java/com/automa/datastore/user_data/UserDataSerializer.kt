@file:Suppress("BlockingMethodInNonBlockingContext")

package com.automa.datastore.user_data

import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

class UserDataSerializer: Serializer<UserDataModel> {
    override val defaultValue: UserDataModel
        get() = UserDataModel()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): UserDataModel {
        return try {
            val readInput = input.readBytes()
            return ProtoBuf.decodeFromByteArray(UserDataModel.serializer(), readInput)
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: UserDataModel, output: OutputStream) {
        val byteArray = ProtoBuf.encodeToByteArray(UserDataModel.serializer(), t)
        output.write(byteArray)
    }
}