package com.chiksmedina.medweather.search.data.network.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NetworkSearch(
    @SerialName("generationtime_ms")
    val generationTimeMs: Double,
    @SerialName("results")
    val results: List<SearchResult>
)

@Serializable
data class SearchResult(
    @SerialName("admin1")
    val admin1: String,
    @SerialName("admin1_id")
    val admin1Id: Int,
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
    val name: String,
    @SerialName("timezone")
    val timezone: String
)