package com.chiksmedina.medweather.main.data

import com.chiksmedina.medweather.main.data.network.WeatherApi
import com.chiksmedina.medweather.main.data.network.models.NetworkWeather
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {
    override suspend fun getWeather(lat: Double, lon: Double): Result<NetworkWeather> =
        api.getWeather(lat, lon)

}