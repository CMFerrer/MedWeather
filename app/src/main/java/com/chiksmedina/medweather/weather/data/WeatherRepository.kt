package com.chiksmedina.medweather.weather.data

import com.chiksmedina.medweather.weather.data.network.models.NetworkWeather

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Result<NetworkWeather>

}