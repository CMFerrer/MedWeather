package com.chiksmedina.medweather.search.data.network

import android.util.Log
import com.chiksmedina.medweather.core.util.HttpRoutes
import com.chiksmedina.medweather.search.data.network.models.NetworkSearch
import com.chiksmedina.medweather.weather.data.network.WeatherApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class SearchApiImpl(
    private val client: HttpClient
): SearchApi {
    override suspend fun search(city: String): Result<NetworkSearch> {
        return try {
            val response: NetworkSearch = client.get(HttpRoutes.SEARCH_COUNTRY){
                url {
                    parameters.append("name", city)
                    parameters.append("count", "10")
                    parameters.append("language", "es")
                    parameters.append("format", "json")
                }
            }.body()
            Result.success(response)
        } catch(e: Exception) {
            Log.d(WeatherApiImpl.TAG, "Error: general ${e.message}")
            Result.failure(Exception("Error al consultar la ciudad"))
        }
    }

    companion object {
        const val TAG = "SearchApiImpl"
    }
}