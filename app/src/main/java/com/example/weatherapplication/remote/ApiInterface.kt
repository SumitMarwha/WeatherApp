package com.example.weatherapplication.remote

import com.example.weatherapplication.data.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v1/forecast?current=temperature_2m,wind_speed_10m,weather_code,apparent_temperature&hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl&forecast_days=10&daily=weather_code,temperature_2m_max,temperature_2m_min")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): Response<WeatherDto>

}