package com.chiksmedina.medweather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.chiksmedina.medweather.navigation.Routes
import com.chiksmedina.medweather.search.navigation.navigateToSearch
import com.chiksmedina.medweather.weather.navigation.navigateToWeather
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        AppState(
            navController,
            coroutineScope,
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowTopBar: Boolean
        @Composable get() = currentDestination?.route != Routes.Weather.route


    val topAppBarTitle: String
        @Composable get() = when (currentDestination?.route) {
            Routes.Weather.route -> "Clima"
            Routes.Search.route -> "Buscar"
            else -> "Weather"
        }

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: Routes) {
        trace("Navigation: ${topLevelDestination.route}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                Routes.Weather -> navController.navigateToWeather(topLevelNavOptions)
                Routes.Search -> navController.navigateToSearch(topLevelNavOptions)
            }
        }
    }

}