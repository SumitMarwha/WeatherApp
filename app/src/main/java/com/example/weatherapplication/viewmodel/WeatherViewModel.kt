package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.data.DailyData
import com.example.weatherapplication.data.HourlyData
import com.example.weatherapplication.data.WeatherDto
import com.example.weatherapplication.data.WeatherType
import com.example.weatherapplication.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    private val _locationName = MutableStateFlow("")
    val locationName = _locationName.asStateFlow()

    private val _weatherResponse = MutableStateFlow<WeatherDto?>(null)
    val weatherResponse = _weatherResponse.asStateFlow()

    fun getWeatherResponse(lat: Double, lng: Double) = viewModelScope.launch {
        weatherRepository.getWeatherResponse(lat, lng).collectLatest {
            setHourlyData(it.data)
            setDailyData(it.data)
            _weatherResponse.value = it.data
        }
    }

    private fun setDailyData(weatherDto: WeatherDto?) {
        val list = ArrayList<DailyData>()
        weatherDto?.dailyWeatherData?.time?.forEachIndexed { index, s ->
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("E, MMM dd", Locale.getDefault())
            val date = inputFormat.parse(s)
            if (date != null) {
                var time = outputFormat.format(date)
                if (index == 0) {
                    time = "Today     "
                }
                list.add(
                    DailyData(
                        time,
                        weatherDto.dailyWeatherData.temperature2mMin?.get(index),
                        weatherDto.dailyWeatherData.temperature2mMax?.get(index),
                        WeatherType.fromWMO(
                            weatherDto.dailyWeatherData.weatherCode?.get(index) ?: 0
                        )
                    )
                )
            }
        }
        weatherDto?.dailyDataList = list
    }

    private fun setHourlyData(weatherDto: WeatherDto?) {
        val list = ArrayList<HourlyData>()
        weatherDto?.hourlyWeatherData?.time?.forEachIndexed { index, s ->
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("hh a", Locale.getDefault())
            val date = inputFormat.parse(s)
            if (date != null && (System.currentTimeMillis() - date.time) < 60*60*1000 && (date.time - System.currentTimeMillis()) < 24*60*60*1000) {
                var time = outputFormat.format(date)
                if ((System.currentTimeMillis() - date.time) >= 0) {
                    time = "Now"
                }
                val hourlyData = HourlyData(time,
                    weatherDto.hourlyWeatherData.temperatures?.get(index),
                    WeatherType.fromWMO(weatherDto.hourlyWeatherData.weatherCodes?.get(index)?:0))
                list.add(hourlyData)
            }
        }
        weatherDto?.hourlyDataList = list
    }

    fun setLocation(location: String) {
        _locationName.value = location
    }
}