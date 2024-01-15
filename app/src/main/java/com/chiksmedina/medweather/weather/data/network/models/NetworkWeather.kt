package com.chiksmedina.medweather.weather.data.network.models
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class NetworkWeather(
    @SerialName("current")
    val current: Current,
    @SerialName("current_units")
    val currentUnits: CurrentUnits,
    @SerialName("daily")
    val daily: Daily,
    @SerialName("daily_units")
    val dailyUnits: DailyUnits,
    @SerialName("elevation")
    val elevation: Int,
    @SerialName("generationtime_ms")
    val generationTimeMs: Double,
    @SerialName("hourly")
    val hourly: Hourly,
    @SerialName("hourly_units")
    val hourlyUnits: HourlyUnits,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Int
)

@Serializable
data class Current(
    @SerialName("interval")
    val interval: Int,
    @SerialName("precipitation")
    val precipitation: Int,
    @SerialName("rain")
    val rain: Int,
    @SerialName("showers")
    val showers: Int,
    @SerialName("temperature_2m")
    val temperature2m: Double,
    @SerialName("time")
    val time: String,
    @SerialName("weather_code")
    val weatherCode: Int,
    @SerialName("wind_speed_10m")
    val windSpeed10m: Double
)

@Serializable
data class CurrentUnits(
    @SerialName("interval")
    val interval: String,
    @SerialName("precipitation")
    val precipitation: String,
    @SerialName("rain")
    val rain: String,
    @SerialName("showers")
    val showers: String,
    @SerialName("temperature_2m")
    val temperature2m: String,
    @SerialName("time")
    val time: String,
    @SerialName("weather_code")
    val weatherCode: String,
    @SerialName("wind_speed_10m")
    val windSpeed10m: String
)

@Serializable
data class Daily(
    @SerialName("sunrise")
    val sunrise: List<String>,
    @SerialName("sunset")
    val sunset: List<String>,
    @SerialName("temperature_2m_max")
    val temperature2mMax: List<Double>,
    @SerialName("temperature_2m_min")
    val temperature2mMin: List<Double>,
    @SerialName("time")
    val time: List<String>
)

@Serializable
data class DailyUnits(
    @SerialName("sunrise")
    val sunrise: String,
    @SerialName("sunset")
    val sunset: String,
    @SerialName("temperature_2m_max")
    val temperature2mMax: String,
    @SerialName("temperature_2m_min")
    val temperature2mMin: String,
    @SerialName("time")
    val time: String
)

@Serializable
data class Hourly(
    @SerialName("temperature_2m")
    val temperature2m: List<Double>,
    @SerialName("time")
    val time: List<String>
)

@Serializable
data class HourlyUnits(
    @SerialName("temperature_2m")
    val temperature2m: String,
    @SerialName("time")
    val time: String
)