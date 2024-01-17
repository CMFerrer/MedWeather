package com.chiksmedina.medweather.ui.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModel

// A sample from the documentation of the graphics library was adapted.
@Composable
fun LineChartHourlyWeatherM3(entryModel: ChartEntryModel, hours: List<String>) {
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> hours[x.toInt() % hours.size] }

    val marker = rememberMarker()
    val mapMarker = mapOf(
        0f to marker, 1f to marker, 2f to marker, 3f to marker, 4f to marker, 5f to marker,
        6f to marker, 7f to marker, 8f to marker, 9f to marker, 10f to marker, 11f to marker,
        12f to marker, 13f to marker, 14f to marker, 15f to marker, 16f to marker, 17f to marker,
        18f to marker, 19f to marker, 20f to marker, 21f to marker, 22f to marker, 23f to marker,
        24f to marker
    )
    ProvideChartStyle(m3ChartStyle()) {
        Chart(
            chart = lineChart(spacing = 85.dp, persistentMarkers = remember(marker) { mapMarker }),
            model = entryModel,
            startAxis = rememberStartAxis(
                label = null,
                guideline = null,
                axis = null,
                tickLength = 0.dp
            ),
            bottomAxis = rememberBottomAxis(
                guideline = null,
                valueFormatter = bottomAxisValueFormatter
            ),
            fadingEdges = rememberFadingEdges(),
            marker = marker,
        )
    }
}