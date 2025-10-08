package com.automa.datastore.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.automa.datastore.user_data.UserDataModel
import com.automa.datastore.user_data.UserDataSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    companion object {
        private const val DATASTORE_FILE = "user-data.pb"
    }

    @Singleton
    @Provides
    fun provideDataStore(application: Application): DataStore<UserDataModel> {
        return DataStoreFactory.create(
            produceFile = { File(application.filesDir, "datastore/$DATASTORE_FILE") },
            serializer = UserDataSerializer()
        )
    }
}