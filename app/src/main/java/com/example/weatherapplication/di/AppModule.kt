package com.example.weatherapplication.di

import com.example.weatherapplication.data.location.DefaultLocationTracker
import com.example.weatherapplication.data.location.LocationTracker
import com.example.weatherapplication.remote.ApiClient
import com.example.weatherapplication.repository.WeatherRepository
import com.example.weatherapplication.repository.WeatherRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single { Gson() }
    single { JsonParser() }
    single { ApiClient() }

    factory<WeatherRepository> { WeatherRepositoryImpl(get()) }

    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(androidApplication()) }
    single<LocationTracker> {
        DefaultLocationTracker(
            get(),
            androidApplication()
        )
    }
}