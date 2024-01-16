package com.chiksmedina.medweather.search.data

import com.chiksmedina.medweather.search.data.network.SearchApi
import com.chiksmedina.medweather.search.data.network.models.NetworkSearch
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi
): SearchRepository {
    override suspend fun search(city: String): Result<NetworkSearch> =
        api.search(city)

}