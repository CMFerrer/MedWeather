package com.chiksmedina.medweather.weather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiksmedina.medweather.weather.data.DataStoreRepository
import com.chiksmedina.medweather.weather.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val data: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        // Fetch weather data when the ViewModel is created
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { WeatherUiState.Loading }

            combine(data.city, data.latitude, data.longitude, ::Triple)
                .collect { (cit, lat, lon) ->
                    repository.getWeather(lat, lon)
                        .onSuccess { weather ->
                            _uiState.update { WeatherUiState.Success(forecast = weather, city = cit) }
                        }
                        .onFailure { error ->
                            _uiState.update { WeatherUiState.Error(message = error.message ?: "Error App") }
                        }
                }

        }
    }

    fun refresh(endRefreshIndicator: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {

            combine(data.city, data.latitude, data.longitude, ::Triple)
                .collect { (cit, lat, lon) ->
                    repository.getWeather(lat, lon)
                        .onSuccess { weather ->
                            _uiState.update { WeatherUiState.Success(forecast = weather, city = cit) }
                        }
                        .onFailure { error ->
                            _uiState.update { WeatherUiState.Error(message = error.message ?: "Error App") }
                        }
                    endRefreshIndicator()
                }

        }
    }
}