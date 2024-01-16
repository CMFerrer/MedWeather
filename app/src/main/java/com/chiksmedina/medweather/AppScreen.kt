package com.chiksmedina.medweather

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.chiksmedina.medweather.navigation.NavGraph
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    appState: AppState = rememberAppState()
) {

    Scaffold { paddingValues ->

        NavGraph(
            appState = appState,
            paddingValues = paddingValues,
        )

    }
}