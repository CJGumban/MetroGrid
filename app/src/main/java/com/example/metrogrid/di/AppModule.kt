package com.example.metrogrid.di

import android.content.Context
import com.example.metrogrid.NetworkObserver
import com.example.metrogrid.data.api.service.TransportService
import com.example.metrogrid.data.db.TransportDb
import com.example.metrogrid.data.repo.TransportRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://v6.bvg.transport.rest/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTransportService(retrofit: Retrofit): TransportService {
        return retrofit.create(TransportService::class.java)
    }

    @Singleton
    @Provides
    fun providePrivateDatabase(@ApplicationContext context: Context) = TransportDb.getInstance(context)

    @Singleton
    @Provides
    fun provideTransportRepo(
        transportDb: TransportDb,
        transportService: TransportService
    ) = TransportRepo(transportService,transportDb)

    @Singleton
    @Provides
    fun provideNetworkObserver(
        @ApplicationContext context: Context)
    : NetworkObserver = NetworkObserver(context)

}