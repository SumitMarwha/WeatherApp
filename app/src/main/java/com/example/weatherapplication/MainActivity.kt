package com.example.weatherapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.example.weatherapplication.location.LocationTracker
import com.example.weatherapplication.presentation.DaysForecastCompose
import com.example.weatherapplication.presentation.HourlyForecastCompose
import com.example.weatherapplication.presentation.LocationHeaderCompose
import com.example.weatherapplication.presentation.MainCardCompose
import com.example.weatherapplication.ui.theme.WeatherApplicationTheme
import com.example.weatherapplication.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val weatherViewModel by viewModel<WeatherViewModel>()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val locationTracker: LocationTracker by inject()
    private var goToSettings = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainUICompose(innerPadding, weatherViewModel)
                }
            }
        }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            getUserLocation()
        }
        permissionLauncher.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        ))
    }

    private fun getUserLocation() {
        lifecycleScope.launch {
            val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (isGpsEnabled) {
                locationTracker.getCurrentLocation()?.let { location ->
                    val geocoder = Geocoder(applicationContext, Locale.getDefault())
                    val addresses: List<Address>? =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    addresses?.let {
                        val cityName: String = addresses[0].getAddressLine(0)
                        weatherViewModel.setLocation(cityName)
                    }
                    weatherViewModel.getWeatherResponse(location.latitude, location.longitude)
                }
            } else {
                showEnableLocationDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (goToSettings) {
            getUserLocation()
        }
    }

    private fun showEnableLocationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Location")
            .setMessage("To use this feature, please enable location services in your device settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open location settings
                goToSettings = true
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent)
            }
            .setNegativeButton("Cancel", null) // Dismiss the dialog if the user cancels
            .setCancelable(false) // Prevent dismissing by tapping outside the dialog
            .show()
    }
}

@Composable
fun MainUICompose(innerPadding: PaddingValues, weatherViewModel: WeatherViewModel?) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color("#354352".toColorInt()))
    ) {
        LocationHeaderCompose(innerPadding = innerPadding, weatherViewModel)
        MainCardCompose(weatherViewModel)
        HourlyForecastCompose(weatherViewModel)
        DaysForecastCompose(weatherViewModel)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherApplicationTheme {
        MainUICompose(PaddingValues(0.dp), null)
    }
}