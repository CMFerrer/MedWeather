package com.chiksmedina.medweather.search.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSearch(
    @SerialName("generationtime_ms")
    val generationTimeMs: Double,
    @SerialName("results")
    val results: List<SearchResult>? = null
)

@Serializable
data class SearchResult(
    @SerialName("admin1")
    val admin1: String = "",
    @SerialName("admin1_id")
    val admin1Id: Int = 0,
    @SerialName("country")
    val country: String,
    @SerialName("country_code")
    val countryCode: String,
    @SerialName("country_id")
    val countryId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("name")
    val name: String
)