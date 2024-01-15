package com.chiksmedina.medweather.weather.ui

import com.chiksmedina.medweather.weather.data.network.models.NetworkWeather

sealed interface WeatherUiState {
    data object Loading : WeatherUiState

    data class Success(
        val forecast: NetworkWeather? = null,
        val latitude: Double = -12.0432, // Lima default
        val longitude: Double = -77.0282,
        val isErrorRecipient: Boolean = false,
        val isErrorCreate: Boolean = false, // error on create recipient or bank account number
        val errorCreate: String = "", // error message
        val isReceptionEnabled: Boolean = false, // button to go ReceptionScreen
    ) : WeatherUiState

}
