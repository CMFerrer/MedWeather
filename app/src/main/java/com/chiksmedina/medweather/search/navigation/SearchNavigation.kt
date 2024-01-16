package com.chiksmedina.medweather.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.chiksmedina.medweather.AppState
import com.chiksmedina.medweather.navigation.Routes

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(Routes.Search.route, navOptions)
}

fun NavGraphBuilder.searchScreen(
    appState: AppState,
    paddingValues: PaddingValues
) {
    composable(
        route = Routes.Search.route,
    ) { backStackEntry ->

        
    }
}