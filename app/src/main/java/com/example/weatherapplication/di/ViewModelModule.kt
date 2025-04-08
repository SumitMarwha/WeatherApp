package com.example.weatherapplication.di

import com.example.weatherapplication.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { WeatherViewModel(get()) }

}