package com.chiksmedina.medweather.search.ui

import com.chiksmedina.medweather.search.data.network.models.NetworkSearch

sealed interface SearchUiState {
    data object Loading : SearchUiState

    data class Success(
        val cities: NetworkSearch? = null
    ) : SearchUiState

    data class Error(val message: String) : SearchUiState
}
