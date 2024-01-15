package com.chiksmedina.medweather.main.ui

import com.chiksmedina.medweather.main.data.network.models.NetworkWeather

sealed interface WeatherUiState {
    data object Loading : WeatherUiState

    data class Details(
        val forecast: NetworkWeather? = null,
        val latitude: Double = -12.0432, // Lima default
        val longitude: Double = -77.0282,
        val isErrorRecipient: Boolean = false,
        val isErrorCreate: Boolean = false, // error on create recipient or bank account number
        val errorCreate: String = "", // error message
        val isReceptionEnabled: Boolean = false, // button to go ReceptionScreen
    ) : WeatherUiState

    data object Success : WeatherUiState
}
