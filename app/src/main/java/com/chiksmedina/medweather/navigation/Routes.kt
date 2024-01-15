package com.chiksmedina.medweather.navigation

sealed class Routes(
    val route: String
) {

    data object Weather : Routes(
        route = "main"
    )

    data object Search : Routes(
        route = "search"
    )

}