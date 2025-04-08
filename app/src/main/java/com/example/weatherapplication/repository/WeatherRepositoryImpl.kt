package com.example.weatherapplication.repository

import com.example.weatherapplication.data.NetworkResult
import com.example.weatherapplication.data.WeatherDto
import com.example.weatherapplication.remote.ApiClient
import com.example.weatherapplication.remote.NetworkResource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(private val apiClient: ApiClient): WeatherRepository {
    override suspend fun getWeatherResponse(lat: Double, lng: Double): Flow<NetworkResult<WeatherDto?>> {
        return NetworkResource(
            { apiClient.getService.getWeatherData(lat, lng) }
        ).query(true)
    }
}