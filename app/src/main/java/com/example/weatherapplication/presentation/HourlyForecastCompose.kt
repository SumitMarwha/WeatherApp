package com.example.weatherapplication.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import com.example.weatherapplication.viewmodel.WeatherViewModel

@Composable
fun HourlyForecastCompose(weatherViewModel: WeatherViewModel?) {
    Column {
        val weatherResponse = weatherViewModel?.weatherResponse?.collectAsStateWithLifecycle(
            LocalLifecycleOwner.current
        )
        when (weatherResponse?.value) {
            null -> {}
            else -> {
                Text(
                    text = "Hourly forecast",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp, 12.dp, 0.dp, 0.dp)
                )
                Card(
                    modifier = Modifier
                        .padding(16.dp, 10.dp, 16.dp, 0.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardColors(
                        containerColor = Color("#26303c".toColorInt()),
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color.White
                    )
                ) {
                    LazyRow(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        itemsIndexed(weatherResponse.value?.hourlyDataList?:listOf()) { index, item ->
                            Column(modifier = Modifier.padding(16.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Text(text = "${item.temperatures}°")
                                Image(modifier = Modifier
                                    .width(30.dp)
                                    .height(50.dp)
                                    .padding(0.dp, 16.dp, 0.dp, 4.dp), painter = painterResource(id = item.weatherCodes?.iconRes?:0), contentDescription = null)
                                Text(text = item.time?:"")
                            }
                        }
                    }
                }
            }
        }
    }
}