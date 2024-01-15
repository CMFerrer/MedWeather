package com.chiksmedina.medweather.weather.data.network

import com.chiksmedina.medweather.weather.data.network.models.NetworkWeather

interface WeatherApi {
    suspend fun getWeather(lat: Double, lon: Double): Result<NetworkWeather>
}