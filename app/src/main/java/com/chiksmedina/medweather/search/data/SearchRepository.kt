package com.chiksmedina.medweather.search.data

import com.chiksmedina.medweather.search.data.network.models.NetworkSearch

interface SearchRepository {
    suspend fun search(city: String): Result<NetworkSearch>
}