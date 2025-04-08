package com.example.weatherapplication.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapplication.viewmodel.WeatherViewModel

@Composable
fun LocationHeaderCompose(innerPadding: PaddingValues, weatherViewModel: WeatherViewModel?) {
    Card(
        modifier = Modifier
            .padding(
                16.dp,
                innerPadding.calculateTopPadding() + 16.dp,
                16.dp,
                innerPadding.calculateBottomPadding()
            )
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardColors(
            containerColor = Color("#26303c".toColorInt()),
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val location = weatherViewModel?.locationName?.collectAsStateWithLifecycle(
                LocalLifecycleOwner.current
            )
            when (location?.value) {
                "" -> {
                    CircularProgressIndicator(modifier = Modifier
                        .padding(10.dp, 0.dp, 0.dp, 0.dp).wrapContentSize(Alignment.Center).size(25.dp))
                }
                else -> {
                    Icon(modifier = Modifier.padding(16.dp, 0.dp, 8.dp, 0.dp), imageVector = Icons.Default.LocationOn, tint = Color.White, contentDescription = "")
                }
            }
            Box (modifier = Modifier.fillMaxWidth(0.88f)) {
                Text(text = location?.value ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(modifier = Modifier.fillMaxHeight(), imageVector = Icons.Default.AccountCircle, tint = Color.White, contentDescription = "")
        }
    }
}