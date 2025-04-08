package com.example.weatherapplication.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapplication.data.WeatherType
import com.example.weatherapplication.viewmodel.WeatherViewModel

@Composable
fun MainCardCompose(weatherViewModel: WeatherViewModel?) {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        val weatherResponse = weatherViewModel?.weatherResponse?.collectAsStateWithLifecycle(
            LocalLifecycleOwner.current
        )
        when (weatherResponse?.value) {
            null -> {}
            else -> {
                val weatherType = WeatherType.fromWMO(weatherResponse.value?.currentWeatherData?.weatherCode?:0)
                Column {
                    Text(text = "Now", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(16.dp, 12.dp, 0.dp, 0.dp))
                    Row (modifier = Modifier.padding(16.dp, 6.dp, 0.dp, 0.dp), verticalAlignment = Alignment.CenterVertically){
                        Text(text = "${weatherResponse.value?.currentWeatherData?.temperature2m}°", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(modifier = Modifier.width(40.dp), painter = painterResource(id = weatherType.iconRes), contentDescription = null)
                    }
                    Row(modifier = Modifier.padding(16.dp, 6.dp, 0.dp, 0.dp)) {
                        Text(text = "High: ", color = Color("#949eab".toColorInt()), fontSize = 16.sp)
                        Text(text = "${weatherResponse.value?.dailyWeatherData?.temperature2mMax?.get(0)}°", color = Color("#949eab".toColorInt()), fontSize = 16.sp)
                        Text(text = " · ", color = Color.White, fontSize = 16.sp)
                        Text(text = "Low: ", color = Color("#949eab".toColorInt()), fontSize = 16.sp)
                        Text(text = "${weatherResponse.value?.dailyWeatherData?.temperature2mMin?.get(0)}°", color = Color("#949eab".toColorInt()), fontSize = 16.sp)
                    }
                }
                Column(modifier = Modifier.padding(0.dp, 6.dp, 16.dp, 0.dp), horizontalAlignment = Alignment.End) {
                    Text(text = weatherType.weatherDesc, color = Color.White, fontSize = 20.sp)
                    Text(text = "Feels Like ${weatherResponse.value?.currentWeatherData?.apparentTemperature}°", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(0.dp, 6.dp, 0.dp, 0.dp))
                }
            }
        }
    }
}