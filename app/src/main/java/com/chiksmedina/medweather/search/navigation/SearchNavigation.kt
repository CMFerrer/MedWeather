package com.chiksmedina.medweather.search.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import com.chiksmedina.medweather.search.ui.SearchScreen
import com.chiksmedina.medweather.search.ui.SearchUiState
import com.chiksmedina.medweather.search.ui.SearchViewModel
import com.chiksmedina.medweather.weather.ui.LoadingUI
import com.chiksmedina.medweather.weather.ui.NoInternetConnection

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(Routes.Search.route, navOptions)
}

fun NavGraphBuilder.searchScreen(
    appState: AppState
) {
    composable(
        route = Routes.Search.route,
        enterTransition = {
            when(initialState.destination.route) {
                Routes.Weather.route -> {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(700)
                    )
                }
                else -> null
            }
        },
        exitTransition = {
            when(targetState.destination.route) {
                Routes.Weather.route -> {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(700)
                    )
                }
                else -> null
            }
        }
    ) {

        val searchViewModel = hiltViewModel<SearchViewModel>()
        val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

        when (uiState) {
            SearchUiState.Loading -> LoadingUI()

            is SearchUiState.Success -> SearchScreen(
                uiState = uiState as SearchUiState.Success,
                scope = appState.coroutineScope,
                search = searchViewModel::search,
                saveCityAndLocation = { cit, lat, lon ->
                    searchViewModel.saveCityAndLocation(cit, lat, lon) {
                        appState.navigateToTopLevelDestination(Routes.Weather)
                    }
                },
                onBackPress = { appState.navController.popBackStack() }
            )

            is SearchUiState.Error -> NoInternetConnection((uiState as SearchUiState.Error).message) {
                searchViewModel.search("")
            }
        }

    }
}