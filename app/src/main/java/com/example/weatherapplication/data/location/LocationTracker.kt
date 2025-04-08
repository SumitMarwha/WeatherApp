package com.example.weatherapplication.data.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}