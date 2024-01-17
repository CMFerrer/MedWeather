package com.chiksmedina.medweather

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.chiksmedina.medweather.navigation.NavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    appState: AppState = rememberAppState()
) {

    NavGraph(appState = appState)
}