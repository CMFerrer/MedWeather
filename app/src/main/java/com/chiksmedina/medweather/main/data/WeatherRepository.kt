package com.chiksmedina.medweather.main.data

import com.chiksmedina.medweather.main.data.network.models.NetworkWeather

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Result<NetworkWeather>

}