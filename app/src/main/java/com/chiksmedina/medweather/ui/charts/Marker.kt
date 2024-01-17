package com.chiksmedina.medweather.ui.charts

import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.chart.values.ChartValuesProvider
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.ceil
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.extension.doubled
import com.patrykandpatrick.vico.core.extension.half
import com.patrykandpatrick.vico.core.extension.orZero
import com.patrykandpatrick.vico.core.marker.Marker

@Composable
internal fun rememberMarker(): Marker {

    val label = textComponent(
        lineCount = LABEL_LINE_COUNT,
        padding = labelPadding,
        typeface = Typeface.MONOSPACE,
    )
    val indicatorInnerComponent = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color.White)

    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue,
        ),
        innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue,
    )

    return remember(label, indicator) {
        object : MarkerComponent(label, indicator, null) {
            init {
                indicatorSizeDp = INDICATOR_SIZE_DP
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color = entryColor.copyColor(INDICATOR_OUTER_COMPONENT_ALPHA)
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS, color = entryColor)
                    }
                }
            }

            private val tempBounds = RectF()

            private val TextComponent.tickSizeDp: Float
                get() = ((background as? ShapeComponent)?.shape as? MarkerCorneredShape)?.tickSizeDp.orZero

            override fun draw(
                context: DrawContext,
                bounds: RectF,
                markedEntries: List<Marker.EntryModel>,
                chartValuesProvider: ChartValuesProvider
            ): Unit = with(context) {
                val halfIndicatorSize = indicatorSizeDp.half.pixels

                markedEntries.forEachIndexed { _, model ->
                    onApplyEntryColor?.invoke(model.color)
                    indicator.draw(
                        context,
                        model.location.x - halfIndicatorSize,
                        model.location.y - halfIndicatorSize,
                        model.location.x + halfIndicatorSize,
                        model.location.y + halfIndicatorSize,
                    )
                }
                drawLabel(context, bounds, markedEntries, chartValuesProvider.getChartValues())
            }

            private fun drawLabel(
                context: DrawContext,
                bounds: RectF,
                markedEntries: List<Marker.EntryModel>,
                chartValues: ChartValues,
            ): Unit = with(context) {
                val text = labelFormatter.getLabel(markedEntries, chartValues)
                val entryX = markedEntries.averageOf { it.location.x }
                val entryY = markedEntries.averageOf { it.location.y }
                val labelBounds =
                    label.getTextBounds(context = context, text = text, width = bounds.width().toInt(), outRect = tempBounds)
                val halfOfTextWidth = labelBounds.width().half
                val x = overrideXPositionToFit(entryX, bounds, halfOfTextWidth)
                this[MarkerCorneredShape.tickXKey] = entryX

                label.drawText(
                    context = context,
                    text = text,
                    textX = x,
                    textY = entryY - labelBounds.height() - label.tickSizeDp.pixels - 36f,
                    verticalPosition = VerticalPosition.Bottom,
                    maxTextWidth = minOf(bounds.right - x, x - bounds.left).doubled.ceil.toInt(),
                )
            }

            private fun <T> Collection<T>.averageOf(selector: (T) -> Float): Float =
                fold(0f) { sum, element ->
                    sum + selector(element)
                } / size

            private fun overrideXPositionToFit(
                xPosition: Float,
                bounds: RectF,
                halfOfTextWidth: Float,
            ): Float = when {
                xPosition - halfOfTextWidth < bounds.left -> bounds.left + halfOfTextWidth
                xPosition + halfOfTextWidth > bounds.right -> bounds.right - halfOfTextWidth
                else -> xPosition
            }

            override fun getInsets(context: MeasureContext, outInsets: Insets, horizontalDimensions: HorizontalDimensions) =
                with(context) {
                    outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
                            LABEL_BACKGROUND_SHADOW_RADIUS.pixels * SHADOW_RADIUS_MULTIPLIER -
                            LABEL_BACKGROUND_SHADOW_DY.pixels
                }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
private const val LABEL_BACKGROUND_SHADOW_DY = 2f
private const val LABEL_LINE_COUNT = 1
private const val INDICATOR_SIZE_DP = 36f
private const val INDICATOR_OUTER_COMPONENT_ALPHA = 32
private const val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
private const val SHADOW_RADIUS_MULTIPLIER = 1.3f

private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
private val labelHorizontalPaddingValue = 8.dp
private val labelVerticalPaddingValue = 4.dp
private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
private val indicatorInnerAndCenterComponentPaddingValue = 5.dp
private val indicatorCenterAndOuterComponentPaddingValue = 10.dp