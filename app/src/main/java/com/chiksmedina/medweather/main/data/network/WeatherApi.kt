package com.chiksmedina.medweather.main.data.network

import com.chiksmedina.medweather.main.data.network.models.NetworkWeather

interface WeatherApi {
    suspend fun getWeather(lat: Double, lon: Double): Result<NetworkWeather>
}