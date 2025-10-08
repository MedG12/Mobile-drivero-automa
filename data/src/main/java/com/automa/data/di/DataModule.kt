package com.automa.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.automa.data.BuildConfig
import com.automa.data.account.AccountRepositoryImpl
import com.automa.data.account.remote.AccountApiClient
import com.automa.data.auth.AuthRepositoryImpl
import com.automa.data.auth.remote.AuthApiClient
import com.automa.data.base.ApiService
import com.automa.data.base.AuthTokenInterceptor
import com.automa.data.base.OkHttpClientFactory
import com.automa.data.breakdown_report.BreakdownReportRepositoryImpl
import com.automa.data.breakdown_report.remote.BreakdownReportApiClient
import com.automa.data.check_sheet.CheckSheetRepositoryImpl
import com.automa.data.check_sheet.remote.CheckSheetApiClient
import com.automa.data.driver_task.DriverTaskRepositoryImpl
import com.automa.data.driver_task.remote.DriverTaskApiClient
import com.automa.data.live_monitoring.LiveMonitoringRepositoryImpl
import com.automa.data.live_monitoring.remote.LiveMonitoringApiClient
import com.automa.data.mechanic_task.MechanicTaskRepositoryImpl
import com.automa.data.mechanic_task.remote.MechanicTaskApiClient
import com.automa.data.qr.QrRepositoryImpl
import com.automa.data.qr.remote.QrApiClient
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.account.AccountRepository
import com.automa.domain.auth.AuthRepository
import com.automa.domain.breakdown_report.BreakdownReportRepository
import com.automa.domain.check_sheet.CheckSheetRepository
import com.automa.domain.driver_task.DriverTaskRepository
import com.automa.domain.live_monitoring.LiveMonitoringRepository
import com.automa.domain.mechanic_task.MechanicTaskRepository
import com.automa.domain.qr.QrRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @AutomaBaseUrl
    @Provides
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @BasicOkHttpClient
    @Provides
    @Singleton
    fun provideBasicOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClientFactory.create(context, BuildConfig.DEBUG, listOf())
    }

    @AuthOkHttpClient
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(@ApplicationContext context: Context, @AuthInterceptor authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClientFactory.create(context, BuildConfig.DEBUG, listOf(authInterceptor))
    }

    @AuthInterceptor
    @Provides
    @Singleton
    fun provideAuthInterceptor(userDataStore: DataStore<UserDataModel>): Interceptor {
        return AuthTokenInterceptor(userDataStore)
    }

    @Provides
    @Singleton
    fun provideDriverAuthService(@BasicOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): AuthApiClient {
        return ApiService.createService(AuthApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideAccountAuthService(@AuthOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): AccountApiClient {
        return ApiService.createService(AccountApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideDriverTaskService(@AuthOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): DriverTaskApiClient {
        return ApiService.createService(DriverTaskApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideMechanicTaskService(@AuthOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): MechanicTaskApiClient {
        return ApiService.createService(MechanicTaskApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideCheckSheetService(@AuthOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): CheckSheetApiClient {
        return ApiService.createService(CheckSheetApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideBreakdownReportService(@AuthOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): BreakdownReportApiClient {
        return ApiService.createService(BreakdownReportApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideQrService(@AuthOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): QrApiClient {
        return ApiService.createService(QrApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideLiveMonitoringService(@AuthOkHttpClient okHttpClient: OkHttpClient, @AutomaBaseUrl baseUrl: String): LiveMonitoringApiClient {
        return ApiService.createService(LiveMonitoringApiClient::class.java, okHttpClient, baseUrl)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApiClient: AuthApiClient): AuthRepository {
        return AuthRepositoryImpl(authApiClient)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(accountApiClient: AccountApiClient): AccountRepository {
        return AccountRepositoryImpl(accountApiClient)
    }

    @Provides
    @Singleton
    fun provideDriverTaskRepository(driverTaskApiClient: DriverTaskApiClient): DriverTaskRepository {
        return DriverTaskRepositoryImpl(driverTaskApiClient)
    }

    @Provides
    @Singleton
    fun provideMechanicTaskRepository(mechanicTaskApiClient: MechanicTaskApiClient): MechanicTaskRepository {
        return MechanicTaskRepositoryImpl(mechanicTaskApiClient)
    }

    @Provides
    @Singleton
    fun provideCheckSheetRepository(checkSheetApiClient: CheckSheetApiClient): CheckSheetRepository {
        return CheckSheetRepositoryImpl(checkSheetApiClient)
    }

    @Provides
    @Singleton
    fun provideBreakdownReportRepository(breakdownReportApiClient: BreakdownReportApiClient): BreakdownReportRepository {
        return BreakdownReportRepositoryImpl(breakdownReportApiClient)
    }

    @Provides
    @Singleton
    fun provideQrRepository(qrApiClient: QrApiClient): QrRepository {
        return QrRepositoryImpl(qrApiClient)
    }

    @Provides
    @Singleton
    fun provideLiveMonitoringRepository(liveMonitoringApiClient: LiveMonitoringApiClient): LiveMonitoringRepository {
        return LiveMonitoringRepositoryImpl(liveMonitoringApiClient)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AutomaBaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BasicOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptor