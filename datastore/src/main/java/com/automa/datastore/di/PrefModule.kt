package com.automa.datastore.di

import android.content.Context
import com.automa.datastore.pref.Preferences
import com.automa.datastore.pref.PreferencesImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefModule {
    @Singleton
    @Provides
    fun provideAppPref(@ApplicationContext context: Context): Preferences {
        return PreferencesImpl(context, "App_Preference", Gson())
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AppPrefProvider {
        fun getPreferences(): Preferences
    }
}