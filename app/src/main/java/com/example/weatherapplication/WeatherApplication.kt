package com.example.weatherapplication

import android.app.Application
import com.example.weatherapplication.di.appModule
import com.example.weatherapplication.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@WeatherApplication)
            modules(listOf(appModule, viewModelModule))
        }
    }
}