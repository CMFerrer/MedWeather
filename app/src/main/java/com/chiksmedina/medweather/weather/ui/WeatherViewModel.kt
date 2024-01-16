package com.chiksmedina.medweather.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiksmedina.medweather.weather.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        // Fetch weather data when the ViewModel is created
        getWeather(lat = -12.0432, lon = -77.0282)
    }

    fun getWeather(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeather(lat, lon)
                .onSuccess { weather ->
                    _uiState.update { WeatherUiState.Success(weather) }
                }
        }
    }
}