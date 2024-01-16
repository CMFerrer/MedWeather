package com.chiksmedina.medweather.core.util

import androidx.compose.ui.graphics.vector.ImageVector
import com.chiksmedina.solar.BoldDuotoneSolar
import com.chiksmedina.solar.boldduotone.Weather
import com.chiksmedina.solar.boldduotone.weather.CloudRain
import com.chiksmedina.solar.boldduotone.weather.CloudSnowfall
import com.chiksmedina.solar.boldduotone.weather.CloudSnowfallMinimalistic
import com.chiksmedina.solar.boldduotone.weather.CloudSun2
import com.chiksmedina.solar.boldduotone.weather.CloudWaterdrop
import com.chiksmedina.solar.boldduotone.weather.CloudWaterdrops
import com.chiksmedina.solar.boldduotone.weather.Clouds
import com.chiksmedina.solar.boldduotone.weather.Fog
import com.chiksmedina.solar.boldduotone.weather.Sun
import com.chiksmedina.solar.boldduotone.weather.Sun2
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

fun Int.weatherInterpretationCode(): String = when (this) {
    0 -> "Cielo despejado"
    1 -> "Principalmente despejado"
    2 -> "Parcialmente nublado"
    3 -> "Nublado"
    45 -> "Niebla"
    48 -> "Niebla de escarcha depositante"
    51 -> "Llovizna ligera"
    53 -> "Llovizna moderada"
    55 -> "Llovizna intensa"
    56 -> "Llovizna helada ligera"
    57 -> "Llovizna helada intensa"
    61 -> "Lluvia ligera"
    63 -> "Lluvia moderada"
    65 -> "Lluvia intensa"
    66 -> "Lluvia helada ligera"
    67 -> "Lluvia helada intensa"
    71 -> "Caída de nieve ligera"
    73 -> "Caída de nieve moderada"
    75 -> "Caída de nieve intensa"
    77 -> "Granos de nieve"
    80 -> "Chubascos ligeros"
    81 -> "Chubascos moderados y violentas"
    82 -> "Chubascos violentos"
    85 -> "Chubascos de nieve ligeros"
    86 -> "Chubascos de nieve intensos"
    else -> ""
}

fun Int.iconByWeatherCode(): ImageVector = when (this) {
    0, 1 -> BoldDuotoneSolar.Weather.Sun2
    2 -> BoldDuotoneSolar.Weather.CloudSun2
    3 -> BoldDuotoneSolar.Weather.Clouds
    45, 48 -> BoldDuotoneSolar.Weather.Fog
    51, 53, 56 -> BoldDuotoneSolar.Weather.CloudWaterdrop
    55, 57 -> BoldDuotoneSolar.Weather.CloudWaterdrops
    61, 63, 65, 66, 67 -> BoldDuotoneSolar.Weather.CloudRain
    71, 73 -> BoldDuotoneSolar.Weather.CloudSnowfall
    75, 77 -> BoldDuotoneSolar.Weather.CloudSnowfallMinimalistic
    80, 81, 82, 85, 86 -> BoldDuotoneSolar.Weather.CloudRain
    else -> BoldDuotoneSolar.Weather.Sun
}

fun String.getHour() = this.split("T")[1]

fun String.getDayOfWeek(): String {
    val dateString = this.split("-")
    val dateTime = LocalDateTime.of(dateString[0].toInt(), dateString[1].toInt(), dateString[2].toInt(), 0, 0)

    return "${dateTime.dayOfWeek.getNameOfDay()} ${dateTime.month.getNameOfMonth()} ${dateTime.dayOfMonth}"
}

fun DayOfWeek.getNameOfDay() =
    this.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        .capitalizeWord()

fun Month.getNameOfMonth() =
    this.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        .capitalizeWord()

fun String.capitalizeWord() =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        .replace(".", "")

fun Int.windDirectionDegreeToText(): String {
    val directions = listOf("N", "NE", "E", "SE", "S", "SO", "O", "NO", "N")
    return directions[(this % 360 / 45)]
}