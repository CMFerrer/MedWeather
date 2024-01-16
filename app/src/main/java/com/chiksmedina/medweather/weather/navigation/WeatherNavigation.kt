package com.chiksmedina.medweather.weather.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.chiksmedina.medweather.weather.ui.NoInternetConnection
import com.chiksmedina.medweather.weather.ui.WeatherScreen
import com.chiksmedina.medweather.weather.ui.WeatherUiState
import com.chiksmedina.medweather.weather.ui.WeatherViewModel

fun NavController.navigateToWeather(navOptions: NavOptions? = null) {
    this.navigate(Routes.Weather.route, navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.weatherScreen(
    appState: AppState,
    paddingValues: PaddingValues
) {
    composable(
        route = Routes.Weather.route,
        enterTransition = {
            when(initialState.destination.route) {
                Routes.Search.route -> {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(700)
                    )
                }
                else -> null
            }
        },
        exitTransition = {
            when(targetState.destination.route) {
                Routes.Search.route -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(700)
                    )
                }
                else -> null
            }
        }
    ) { backStackEntry ->

        val weatherViewModel = hiltViewModel<WeatherViewModel>()
        val uiState by weatherViewModel.uiState.collectAsStateWithLifecycle()

        when (uiState) {
            WeatherUiState.Loading -> LoadingUI()

            is WeatherUiState.Success -> WeatherScreen(
                paddingValues = paddingValues,
                uiState = uiState as WeatherUiState.Success,
                pullRefreshState = appState.pullRefreshState,
                toSearch = { appState.navigateToTopLevelDestination(Routes.Search) },
                refresh = { weatherViewModel.refresh { appState.pullRefreshState.endRefresh() } }
            )

            is WeatherUiState.Error -> NoInternetConnection((uiState as WeatherUiState.Error).message) {
                weatherViewModel.getWeather()
            }
        }
    }
}