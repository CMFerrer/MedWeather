package com.chiksmedina.medweather.weather.ui

import com.chiksmedina.medweather.weather.data.network.models.NetworkWeather

sealed interface WeatherUiState {
    data object Loading : WeatherUiState

    data class Success(
        val forecast: NetworkWeather? = null,
        val latitude: Double = -12.0432, // Lima default
        val longitude: Double = -77.0282,
        val city: String = "Lima",
        val errorCreate: String = "", // error message
    ) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}
