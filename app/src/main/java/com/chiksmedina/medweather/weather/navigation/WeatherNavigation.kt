package com.chiksmedina.medweather.weather.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.chiksmedina.medweather.AppState
import com.chiksmedina.medweather.navigation.Routes
import com.chiksmedina.medweather.weather.ui.LoadingUI
import com.chiksmedina.medweather.weather.ui.WeatherScreen
import com.chiksmedina.medweather.weather.ui.WeatherUiState
import com.chiksmedina.medweather.weather.ui.WeatherViewModel

fun NavController.navigateToWeather(navOptions: NavOptions? = null) {
    this.navigate(Routes.Weather.route, navOptions)
}

fun NavGraphBuilder.weatherScreen(
    appState: AppState,
    paddingValues: PaddingValues
) {
    composable(
        route = Routes.Weather.route,
    ) { backStackEntry ->

        val weatherViewModel = hiltViewModel<WeatherViewModel>()
        val uiState by weatherViewModel.uiState.collectAsStateWithLifecycle()

        when (uiState) {
            WeatherUiState.Loading -> LoadingUI()
            is WeatherUiState.Success -> WeatherScreen(
                paddingValues = paddingValues,
                uiState = uiState as WeatherUiState.Success,
                toSearch = { appState.navigateToTopLevelDestination(Routes.Search) }
            )
        }
    }
}