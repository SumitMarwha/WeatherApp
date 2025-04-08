package com.example.weatherapplication.repository

import com.example.weatherapplication.data.NetworkResult
import com.example.weatherapplication.data.WeatherDto
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherResponse(lat: Double, lng: Double): Flow<NetworkResult<WeatherDto?>>
}