package com.example.di

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.location.LocationManager
import com.example.data.remote.ApiService
import com.example.data.remote.AuthInterceptor
import com.example.data.repository.AuthRepository
import com.example.data.repository.ChatRepository
import com.example.data.repository.MechanicRepository
import com.example.data.repository.OrderRepository
import com.example.data.session.SessionManager
import com.example.data.socket.SocketManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(private val context: Context) {

    val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val locationManager: LocationManager by lazy {
        LocationManager(context)
    }

    val socketManager: SocketManager by lazy {
        SocketManager(sessionManager)
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://m4chanic-app-production.up.railway.app/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(apiService, sessionManager, database.userDao())
    }

    val mechanicRepository: MechanicRepository by lazy {
        MechanicRepository(apiService, sessionManager)
    }

    val orderRepository: OrderRepository by lazy {
        OrderRepository(context, apiService, sessionManager, database.orderDao())
    }

    val chatRepository: ChatRepository by lazy {
        ChatRepository(apiService, socketManager, sessionManager)
    }

    val adminRepository: com.example.data.repository.AdminRepository by lazy {
        com.example.data.repository.AdminRepository(apiService, sessionManager)
    }
}
