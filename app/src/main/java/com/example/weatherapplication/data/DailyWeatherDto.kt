package com.example.weatherapplication.data

import com.google.gson.annotations.SerializedName

data class DailyWeatherDto(
    @SerializedName("time")
    val time: ArrayList<String>? = null,
    @SerializedName("weather_code")
    val weatherCode: ArrayList<Int>? = null,
    @SerializedName("temperature_2m_max")
    val temperature2mMax: ArrayList<Double>? = null,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: ArrayList<Double>? = null
)