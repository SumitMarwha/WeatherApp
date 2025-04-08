package com.example.weatherapplication.data

import com.google.gson.annotations.SerializedName

data class WeatherDto (
    @SerializedName("current")
    val currentWeatherData: CurrentWeatherDto? = null,
    @SerializedName("hourly")
    val hourlyWeatherData: WeatherDataDto? = null,
    @SerializedName("daily")
    val dailyWeatherData: DailyWeatherDto? = null,
    var hourlyDataList: List<HourlyData>? = null,
    var dailyDataList: List<DailyData>? = null
)