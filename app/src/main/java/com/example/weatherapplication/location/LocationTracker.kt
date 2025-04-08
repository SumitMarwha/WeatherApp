package com.example.weatherapplication.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}