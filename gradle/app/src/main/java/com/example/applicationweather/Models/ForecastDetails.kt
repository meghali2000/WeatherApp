package com.example.applicationweather.Models

data class ForecastDetails(
    val current: CurrentX,
    val forecast: Forecast,
    val location: LocationX
)