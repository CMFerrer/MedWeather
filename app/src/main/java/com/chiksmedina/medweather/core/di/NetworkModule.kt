package com.chiksmedina.medweather.core.di

import android.util.Log
import com.chiksmedina.medweather.core.util.HttpRoutes
import com.chiksmedina.medweather.search.data.network.SearchApi
import com.chiksmedina.medweather.search.data.network.SearchApiImpl
import com.chiksmedina.medweather.weather.data.network.WeatherApi
import com.chiksmedina.medweather.weather.data.network.WeatherApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("HttpClient", message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true} )
            }
            install(DefaultRequest) {
                url(HttpRoutes.BASE_URL)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                url {
                    parameters.append("current", "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,rain,showers,weather_code,cloud_cover,wind_speed_10m,wind_direction_10m")
                    parameters.append("hourly", "temperature_2m,weather_code")
                    parameters.append("daily", "temperature_2m_max,temperature_2m_min,sunrise,sunset,weather_code")
                    parameters.append("timezone", "auto")
                    parameters.append("forecast_days", "6")
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideWeatherApi(httpClient: HttpClient): WeatherApi {
        return WeatherApiImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideSearchApi(httpClient: HttpClient): SearchApi {
        return SearchApiImpl(httpClient)
    }
}