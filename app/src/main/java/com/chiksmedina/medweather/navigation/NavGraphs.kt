package com.chiksmedina.medweather.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.chiksmedina.medweather.AppState
import com.chiksmedina.medweather.search.navigation.searchScreen
import com.chiksmedina.medweather.weather.navigation.weatherScreen

@Composable
fun NavGraph(
    appState: AppState,
    paddingValues: PaddingValues,
) {

    NavHost(
        navController = appState.navController,
        startDestination = Routes.Weather.route
    ) {
        weatherScreen(appState = appState, paddingValues = paddingValues)
        searchScreen(appState = appState, paddingValues = paddingValues)
    }
}