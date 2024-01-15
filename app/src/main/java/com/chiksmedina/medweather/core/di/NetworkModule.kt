package com.chiksmedina.medweather.core.di

import android.util.Log
import com.chiksmedina.medweather.core.util.HttpRoutes
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
            }
        }
    }
}