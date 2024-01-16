package com.chiksmedina.medweather.search.data.network

import com.chiksmedina.medweather.search.data.network.models.NetworkSearch

interface SearchApi {
    suspend fun search(city: String): Result<NetworkSearch>
}