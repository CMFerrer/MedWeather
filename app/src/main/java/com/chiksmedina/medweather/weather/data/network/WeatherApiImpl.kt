package com.chiksmedina.medweather.weather.data.network

import android.util.Log
import com.chiksmedina.medweather.core.util.HttpRoutes
import com.chiksmedina.medweather.weather.data.network.models.NetworkWeather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherApiImpl(
    private val client: HttpClient
): WeatherApi {
    override suspend fun getWeather(lat: Double, lon: Double): Result<NetworkWeather> {
        return try {
            val response: NetworkWeather = client.get(HttpRoutes.FORECAST){
                url {
                    parameters.append("latitude", lat.toString())
                    parameters.append("longitude", lon.toString())
                }
            }.body()
            Result.success(response)
        } catch(e: Exception) {
            Log.d(TAG, "Error: general ${e.message}")
            Result.failure(Exception("Error al consultar el clima"))
        }
    }

    companion object {
        const val TAG = "WeatherApiImpl"
    }
}