package com.example.weatherapplication.data

data class DailyData(
    val time: String? = null,
    val temperaturesMin: Double? = null,
    val temperaturesMax: Double? = null,
    val weatherCodes: WeatherType? = null
)