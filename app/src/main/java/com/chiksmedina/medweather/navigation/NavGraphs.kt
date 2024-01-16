package com.chiksmedina.medweather.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.chiksmedina.medweather.AppState
import com.chiksmedina.medweather.search.navigation.searchScreen
import com.chiksmedina.medweather.weather.navigation.weatherScreen

@Composable
fun NavGraph(
    appState: AppState
) {

    NavHost(
        navController = appState.navController,
        startDestination = Routes.Weather.route
    ) {
        weatherScreen(appState = appState)
        searchScreen(appState = appState)
    }
}