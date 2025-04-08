package com.example.weatherapplication.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private var retrofit: Retrofit? = null
    val getService: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com")
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original: Request = chain.request()
                chain.proceed(original)
            }
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}