package com.example.weatherapplication.data

import com.google.gson.annotations.SerializedName

data class CurrentWeatherDto(
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("interval")
    val interval: Int? = null,
    @SerializedName("temperature_2m")
    val temperature2m: Double? = null,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double? = null,
    @SerializedName("weather_code")
    val weatherCode: Int? = null,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double? = null
)