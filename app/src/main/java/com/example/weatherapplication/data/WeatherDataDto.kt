package com.example.weatherapplication.data

import com.google.gson.annotations.SerializedName

data class WeatherDataDto(
    val time: List<String>? = null,
    @SerializedName("temperature_2m")
    val temperatures: List<Double>? = null,
    @SerializedName("weathercode")
    val weatherCodes: List<Int>? = null,
    @SerializedName("pressure_msl")
    val pressures: List<Double>? = null,
    @SerializedName("windspeed_10m")
    val windSpeeds: List<Double>? = null,
    @SerializedName("relativehumidity_2m")
    val humidities: List<Double>? = null
)