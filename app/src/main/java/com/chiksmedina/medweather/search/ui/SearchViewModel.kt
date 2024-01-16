package com.chiksmedina.medweather.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiksmedina.medweather.search.data.SearchRepository
import com.chiksmedina.medweather.weather.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val data: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Success())
    val uiState = _uiState.asStateFlow()

    fun search(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { SearchUiState.Loading }
            repository.search(city)
                .onSuccess {  result ->
                        _uiState.update { SearchUiState.Success(result) }
                    }
                .onFailure { error ->
                    _uiState.update { SearchUiState.Error(message = error.message ?: "Error App") }
                }
        }
    }

    fun saveCityAndLocation(city: String, latitude: Double, longitude: Double, toWeather: () -> Unit) {
        viewModelScope.launch {
            data.saveCity(city)
            data.saveLocation(latitude, longitude)
            toWeather()
        }
    }
}