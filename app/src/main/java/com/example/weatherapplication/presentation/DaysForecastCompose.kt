package com.example.weatherapplication.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapplication.viewmodel.WeatherViewModel

@Composable
fun DaysForecastCompose(weatherViewModel: WeatherViewModel?) {
    Column {
        val weatherResponse = weatherViewModel?.weatherResponse?.collectAsStateWithLifecycle(
            LocalLifecycleOwner.current
        )
        when (weatherResponse?.value) {
            null -> {}
            else -> {
                Text(
                    text = "10-day forecast",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp, 12.dp, 0.dp, 0.dp)
                )
                LazyColumn(
                    modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 0.dp)
                ) {
                    itemsIndexed(weatherResponse.value?.dailyDataList?: listOf()) { index, item ->
                        Row(
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                                .background(Color("#26303c".toColorInt()))
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.time?:"",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Image(modifier = Modifier.width(40.dp).height(40.dp), painter = painterResource(id = item.weatherCodes?.iconRes ?: 0), contentDescription = null)
                            Text(text = "${item.temperaturesMax}°/ ${item.temperaturesMin}")
                        }
                    }
                }
            }
        }
    }
}
