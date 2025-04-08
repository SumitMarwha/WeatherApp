package com.example.weatherapplication.data

data class HourlyData(
    val time: String? = null,
    val temperatures: Double? = null,
    val weatherCodes: WeatherType? = null
)