package com.chiksmedina.medweather.weather.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chiksmedina.medweather.core.util.getDayOfWeek
import com.chiksmedina.medweather.core.util.getHour
import com.chiksmedina.medweather.core.util.iconByWeatherCode
import com.chiksmedina.medweather.core.util.weatherInterpretationCode
import com.chiksmedina.medweather.core.util.windDirectionDegreeToText
import com.chiksmedina.medweather.weather.data.network.models.Current
import com.chiksmedina.medweather.weather.data.network.models.CurrentUnits
import com.chiksmedina.medweather.weather.data.network.models.Daily
import com.chiksmedina.medweather.weather.data.network.models.DailyUnits
import com.chiksmedina.medweather.weather.data.network.models.Hourly
import com.chiksmedina.medweather.weather.data.network.models.HourlyUnits
import com.chiksmedina.solar.OutlineSolar
import com.chiksmedina.solar.outline.EssentionalUi
import com.chiksmedina.solar.outline.Weather
import com.chiksmedina.solar.outline.essentionalui.Flag2
import com.chiksmedina.solar.outline.essentionalui.Waterdrop
import com.chiksmedina.solar.outline.weather.Sunrise
import com.chiksmedina.solar.outline.weather.Sunset
import com.chiksmedina.solar.outline.weather.Temperature
import com.chiksmedina.solar.outline.weather.Waterdrops
import com.chiksmedina.solar.outline.weather.Wind
import java.time.LocalDateTime

@Composable
fun WeatherScreen(
    paddingValues: PaddingValues,
    uiState: WeatherUiState.Success,
    toSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        uiState.forecast?.let {
            City(city = uiState.city, toSearch = toSearch)
            WeatherHeader(
                "${it.current.temperature2m} ${it.currentUnits.temperature2m}",
                "${it.daily.temperature2mMax[0]} ${it.dailyUnits.temperature2mMax[0]}",
                "${it.daily.temperature2mMin[0]} ${it.dailyUnits.temperature2mMin[0]}",
                it.current.weatherCode.weatherInterpretationCode()
            )

            Spacer(modifier = Modifier.height(24.dp))

            HourlyWeather(it.hourlyUnits, it.hourly)

            NextFiveDays(it.dailyUnits, it.daily)

            UsefulInformation(it.currentUnits, it.current)

            SunriseAndSunset(it.daily.sunrise[0].split("T")[1], it.daily.sunset[0].split("T")[1])

        }

    }
}

@Composable
fun City(city: String, toSearch: () -> Unit) {
    TextButton(onClick = { toSearch() }) {
        Text(
            text = city,
            modifier = Modifier.padding(horizontal = 18.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun WeatherHeader(
    actualTemperature: String,
    maxTemperature: String,
    minTemperature: String,
    weatherInterpretation: String
) {
    Text(
        text = actualTemperature,
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 16.dp)
    )
    Row {
        Text(
            text = maxTemperature,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(text = " / ")
        Text(
            text = minTemperature,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    Text(text = weatherInterpretation)

}

@Composable
fun HourlyWeather(hourlyUnits: HourlyUnits, hourly: Hourly) {

    val dateTime = LocalDateTime.now()
    val currentHour = hourly.time.find { it.startsWith(dateTime.toString().substring(0, 13)) }

    // New list with the next 24 hours, starting from the current hour
    val timeList =
        hourly.time.subList(hourly.time.indexOf(currentHour), hourly.time.indexOf(currentHour) + 25)
    val temperatureList = hourly.temperature2m.subList(
        hourly.time.indexOf(currentHour),
        hourly.time.indexOf(currentHour) + 25
    )
    val iconList = hourly.weatherCode.subList(
        hourly.time.indexOf(currentHour),
        hourly.time.indexOf(currentHour) + 25
    )

    Card {
        Text(
            text = "Por horas",
            modifier = Modifier.padding(start = 4.dp, top = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(timeList.size) {
                HourlyWeatherCard(
                    hour = timeList[it].getHour(),
                    temperature = "${temperatureList[it]}${hourlyUnits.temperature2m}",
                    icon = iconList[it].iconByWeatherCode()
                )
            }
        }
    }

}

@Composable
fun HourlyWeatherCard(hour: String, temperature: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = hour, style = MaterialTheme.typography.labelMedium)
        Icon(
            imageVector = icon,
            contentDescription = "weather icon",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text = temperature, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun NextFiveDays(dailyUnits: DailyUnits, daily: Daily) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Próximos 5 dias",
            modifier = Modifier.padding(start = 4.dp, top = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {

            daily.time.forEachIndexed { index, time ->
                val maxTemp = "${daily.temperature2mMax[index]}${dailyUnits.temperature2mMax}"
                val minTemp = "${daily.temperature2mMin[index]}${dailyUnits.temperature2mMin}"
                val icon = daily.weatherCode[index].iconByWeatherCode()
                NextFiveDaysCard(time.getDayOfWeek(), maxTemp, minTemp, icon)
            }
        }
    }
}

@Composable
fun NextFiveDaysCard(dayOfWeek: String, maxTemp: String, minTemp: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = dayOfWeek, style = MaterialTheme.typography.titleMedium)
        Icon(
            imageVector = icon,
            contentDescription = "weather icon",
            tint = MaterialTheme.colorScheme.primary
        )
        Row {
            Text(text = maxTemp, style = MaterialTheme.typography.titleSmall)
            Text(text = " / ")
            Text(text = minTemp, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun UsefulInformation(currentUnits: CurrentUnits, current: Current) {
    val modifierRow = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    Card(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "Información útil",
            modifier = Modifier.padding(start = 4.dp, top = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )

        Row(modifier = modifierRow) {
            UsefulInformationCard(
                modifier = Modifier.weight(1f),
                icon = OutlineSolar.EssentionalUi.Waterdrop,
                text = "${current.relativeHumidity2m} ${currentUnits.relativeHumidity2m}",
                subText = "Humedad"
            )
            UsefulInformationCard(
                modifier = Modifier.weight(1f),
                icon = OutlineSolar.Weather.Temperature,
                text = "${current.apparentTemperature} ${currentUnits.apparentTemperature}",
                subText = "ST"
            )
        }
        Row(modifier = modifierRow) {
            UsefulInformationCard(
                modifier = Modifier.weight(1f),
                icon = OutlineSolar.Weather.Wind,
                text = "${current.windSpeed10m} ${currentUnits.windSpeed10m}",
                subText = "Velocidad del Viento"
            )
            UsefulInformationCard(
                modifier = Modifier.weight(1f),
                icon = OutlineSolar.EssentionalUi.Flag2,
                text = current.windDirection10m.windDirectionDegreeToText(),
                subText = "Dirección del viento"
            )
        }
        Row(modifier = modifierRow) {
            UsefulInformationCard(
                modifier = Modifier.weight(1f),
                icon = OutlineSolar.Weather.Wind,
                text = "${current.cloudCover} ${currentUnits.cloudCover}",
                subText = "Cielo cubierto"
            )
            UsefulInformationCard(
                modifier = Modifier.weight(1f),
                icon = OutlineSolar.Weather.Waterdrops,
                text = "${current.precipitation} ${currentUnits.precipitation}",
                subText = "Precipitación"
            )
        }
    }

}

@Composable
fun UsefulInformationCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    subText: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = subText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }

}

@Composable
fun SunriseAndSunset(sunrise: String, sunset: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        ElevatedCard(modifier = Modifier.weight(1f)) {
            UsefulInformationCard(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                icon = OutlineSolar.Weather.Sunrise,
                text = sunrise,
                subText = "Amanecer"
            )
        }
        ElevatedCard(modifier = Modifier.weight(1f)) {
            UsefulInformationCard(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                icon = OutlineSolar.Weather.Sunset,
                text = sunset,
                subText = "Atardecer"
            )
        }
    }
}

@Composable
fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoInternetConnection(message: String, refresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Button(onClick = { refresh() }, shape = MaterialTheme.shapes.medium) {
                Text(text = "Reintentar")
            }
        }

    }
}