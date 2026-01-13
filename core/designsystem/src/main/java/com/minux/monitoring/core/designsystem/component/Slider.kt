package com.minux.monitoring.core.designsystem.component

import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults.TickSize
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.minux.monitoring.core.designsystem.theme.DarkBlue10
import com.minux.monitoring.core.designsystem.theme.HalfTransparentWhite
import com.minux.monitoring.core.designsystem.theme.MNXTheme
import com.minux.monitoring.core.designsystem.theme.MNXTypography
import com.minux.monitoring.core.designsystem.theme.Turquoise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MNXSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    @IntRange(from = 0) steps: Int = 0,
    label: (Float) -> String = { it.toInt().toString() }
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        val density = LocalDensity.current
        val drawPadding = with(density) { MNXSliderTokens.ContentPadding.toPx() }

        val labelPadding = with(density) { MNXSliderTokens.LabelsVerticalPadding.toPx() }
        val textMeasurer = rememberTextMeasurer()

        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .padding(vertical = 20.dp)
                .drawWithContent {
                    drawContent()

                    val sliderOffset = SliderOffset(
                        contentSize = size,
                        contentPadding = Size(
                            width = drawPadding,
                            height = size.height + labelPadding
                        )
                    )

                    drawSliderLabels(
                        textMeasurer = textMeasurer,
                        sliderOffset = sliderOffset,
                        startLabel = label(valueRange.start),
                        endLabel = label(valueRange.endInclusive)
                    )
                },
            thumb = { MNXSliderDefaults.Thumb() },
            track = {
                MNXSliderDefaults.Track(
                    sliderState = it,
                    steps = steps
                )
            },
            valueRange = valueRange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MNXRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    @IntRange(from = 0) steps: Int = 0,
    label: (Float) -> String = { it.toInt().toString() }
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        val density = LocalDensity.current
        val drawPadding = with(density) { MNXSliderTokens.ContentPadding.toPx() }

        val labelPadding = with(density) { MNXSliderTokens.LabelsVerticalPadding.toPx() }
        val textMeasurer = rememberTextMeasurer()

        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .padding(vertical = 20.dp)
                .drawWithContent {
                    drawContent()

                    val sliderOffset = SliderOffset(
                        contentSize = size,
                        contentPadding = Size(
                            width = drawPadding,
                            height = size.height + labelPadding
                        )
                    )

                    drawSliderLabels(
                        textMeasurer = textMeasurer,
                        sliderOffset = sliderOffset,
                        startLabel = label(valueRange.start),
                        endLabel = label(valueRange.endInclusive)
                    )
                },
            startThumb = { MNXSliderDefaults.Thumb() },
            endThumb = { MNXSliderDefaults.Thumb() },
            track = {
                MNXSliderDefaults.Track(
                    rangeSliderState = it,
                    steps = steps
                )
            },
            valueRange = valueRange,
        )
    }
}

private object MNXSliderTokens {
    val ActiveTrackColor = Turquoise.copy(alpha = 0.5f)
    val ContentPadding = 10.dp
    val InactiveTrackColor = DarkBlue10
    val LabelsVerticalPadding = 2.dp
    val TrackHeight = 6.dp
    val TrackCornerSize = 0.dp
    val TrackBorderWidth = 0.5.dp
}

private object MNXSliderDefaults {

    @Composable
    fun Thumb(modifier: Modifier = Modifier) {
        val thumbColor = MaterialTheme.colorScheme.primary

        Box(
            modifier = modifier
                .size(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color = thumbColor)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Track(
        sliderState: SliderState,
        @IntRange(from = 0) steps: Int,
        modifier: Modifier = Modifier,
        drawTick: DrawScope.(Offset, Color) -> Unit = { offset, color ->
            drawStopIndicator(
                drawScope = this,
                offset = offset,
                color = color,
                size = TickSize
            )
        }
    ) {
        val borderColor = MaterialTheme.colorScheme.primary

        Canvas(
            modifier
                .fillMaxWidth()
                .height(MNXSliderTokens.TrackHeight)
                .rotate(if (LocalLayoutDirection.current == LayoutDirection.Rtl) 180f else 0f)
        ) {
            val activeRangeFraction = calcFraction(
                range = sliderState.valueRange,
                position = sliderState.value.coerceIn(sliderState.valueRange)
            )

            drawTrack(
                tickFractions = stepsToTickFractions(steps = steps),
                activeRangeStart = 0f,
                activeRangeEnd = activeRangeFraction,
                inactiveTrackColor = MNXSliderTokens.InactiveTrackColor,
                activeTrackColor = MNXSliderTokens.ActiveTrackColor,
                borderColor = borderColor,
                height = MNXSliderTokens.TrackHeight,
                startThumbWidth = 0.toDp(),
                endThumbWidth = 20.toDp(),
                thumbTrackGapSize = 0.dp,
                trackInsideCornerSize = MNXSliderTokens.TrackCornerSize,
                drawTick = drawTick,
                isRangeSlider = false
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Track(
        rangeSliderState: RangeSliderState,
        @IntRange(from = 0) steps: Int,
        modifier: Modifier = Modifier,
        drawTick: DrawScope.(Offset, Color) -> Unit = { offset, color ->
            drawStopIndicator(
                drawScope = this,
                offset = offset,
                color = color,
                size = TickSize
            )
        }
    ) {
        val borderColor = MaterialTheme.colorScheme.primary

        Canvas(
            modifier
                .fillMaxWidth()
                .height(MNXSliderTokens.TrackHeight)
                .rotate(if (LocalLayoutDirection.current == LayoutDirection.Rtl) 180f else 0f)
        ) {
            val activeRangeStartFraction = calcFraction(
                range = rangeSliderState.valueRange,
                position = rangeSliderState.activeRangeStart.coerceIn(rangeSliderState.valueRange)
            )

            val activeRangeEndFraction = calcFraction(
                range = rangeSliderState.valueRange,
                position = rangeSliderState.activeRangeEnd.coerceIn(rangeSliderState.valueRange)
            )

            drawTrack(
                tickFractions = stepsToTickFractions(steps = steps),
                activeRangeStart = activeRangeStartFraction,
                activeRangeEnd = activeRangeEndFraction,
                inactiveTrackColor = MNXSliderTokens.InactiveTrackColor,
                activeTrackColor = MNXSliderTokens.ActiveTrackColor,
                borderColor = borderColor,
                height = MNXSliderTokens.TrackHeight,
                startThumbWidth = 0.toDp(),
                endThumbWidth = 20.toDp(),
                thumbTrackGapSize = 0.dp,
                trackInsideCornerSize = MNXSliderTokens.TrackCornerSize,
                drawTick = drawTick,
                isRangeSlider = true
            )
        }
    }

    private fun DrawScope.drawTrack(
        tickFractions: FloatArray,
        activeRangeStart: Float,
        activeRangeEnd: Float,
        inactiveTrackColor: Color,
        activeTrackColor: Color,
        borderColor: Color,
        height: Dp,
        startThumbWidth: Dp,
        endThumbWidth: Dp,
        thumbTrackGapSize: Dp,
        trackInsideCornerSize: Dp,
        drawTick: DrawScope.(Offset, Color) -> Unit,
        isRangeSlider: Boolean
    ) {
        val sliderStart = Offset(x = 0f, y = center.y)
        val sliderEnd = Offset(x = size.width, y = center.y)
        val trackStrokeWidth = height.toPx()

        val sliderValueEnd = Offset(
            x = sliderStart.x + (sliderEnd.x - sliderStart.x) * activeRangeEnd,
            y = center.y
        )

        val sliderValueStart = Offset(
            x = sliderStart.x + (sliderEnd.x - sliderStart.x) * activeRangeStart,
            y = center.y
        )

        val cornerSize = trackStrokeWidth / 2
        val insideCornerSize = trackInsideCornerSize.toPx()
        var startGap = 0f
        var endGap = 0f

        if (thumbTrackGapSize > 0.dp) {
            startGap = startThumbWidth.toPx() / 2 + thumbTrackGapSize.toPx()
            endGap = endThumbWidth.toPx() / 2 + thumbTrackGapSize.toPx()
        }

        if (isRangeSlider && sliderValueStart.x > sliderStart.x + startGap + cornerSize) {
            val start = sliderStart.x
            val end = sliderValueStart.x - startGap
            drawTrackPath(
                offset = Offset.Zero,
                size = Size(end - start, trackStrokeWidth),
                color = inactiveTrackColor,
                startCornerRadius = cornerSize,
                endCornerRadius = insideCornerSize
            )
        }

        if (sliderValueEnd.x < sliderEnd.x - endGap - cornerSize) {
            val start = sliderValueEnd.x + endGap
            val end = sliderEnd.x
            drawTrackPath(
                offset = Offset(start, 0f),
                size = Size(end - start, trackStrokeWidth),
                color = inactiveTrackColor,
                startCornerRadius = insideCornerSize,
                endCornerRadius = cornerSize
            )
        }

        val activeTrackStart = if (isRangeSlider) sliderValueStart.x + startGap else 0f
        val activeTrackEnd = sliderValueEnd.x - endGap
        val startCornerRadius = if (isRangeSlider) insideCornerSize else cornerSize

        if (activeTrackEnd - activeTrackStart > startCornerRadius) {
            drawTrackPath(
                offset = Offset(activeTrackStart, 0f),
                size = Size(activeTrackEnd - activeTrackStart, trackStrokeWidth),
                color = activeTrackColor,
                startCornerRadius = startCornerRadius,
                endCornerRadius = insideCornerSize
            )
        }

        val start = Offset(sliderStart.x + cornerSize, sliderStart.y)
        val end = Offset(sliderEnd.x - cornerSize, sliderEnd.y)
        val tickStartGap = sliderValueStart.x - startGap..sliderValueStart.x + startGap
        val tickEndGap = sliderValueEnd.x - endGap..sliderValueEnd.x + endGap

        tickFractions.forEachIndexed { index, tick ->
            val outsideFraction = tick > activeRangeEnd || tick < activeRangeStart
            val center = Offset(x = lerp(start, end, tick).x, y = center.y)

            if ((isRangeSlider && center.x in tickStartGap) || center.x in tickEndGap) {
                return@forEachIndexed
            }

            drawTick(
                this,
                center,
                if (outsideFraction)
                    MNXSliderTokens.ActiveTrackColor
                else
                    MNXSliderTokens.InactiveTrackColor
            )
        }

        drawRoundRect(
            topLeft = Offset(x = 0f, y = center.y - cornerSize),
            size = Size(width = size.width, height = trackStrokeWidth),
            cornerRadius = CornerRadius(x = insideCornerSize, y = insideCornerSize),
            color = borderColor,
            style = Stroke(width = MNXSliderTokens.TrackBorderWidth.toPx())
        )
    }

    private fun DrawScope.drawTrackPath(
        offset: Offset,
        size: Size,
        color: Color,
        startCornerRadius: Float,
        endCornerRadius: Float
    ) {
        val startCorner = CornerRadius(startCornerRadius, startCornerRadius)
        val endCorner = CornerRadius(endCornerRadius, endCornerRadius)
        val track = RoundRect(
            rect = Rect(
                offset = Offset(offset.x, 0f),
                size = Size(size.width, size.height)
            ),
            topLeft = startCorner,
            topRight = endCorner,
            bottomRight = endCorner,
            bottomLeft = startCorner
        )

        trackPath.addRoundRect(track)
        drawPath(path = trackPath, color = color)
        trackPath.rewind()
    }

    private fun drawStopIndicator(drawScope: DrawScope, offset: Offset, size: Dp, color: Color) {
        with(drawScope) {
            drawCircle(color = color, center = offset, radius = size.toPx() / 2f)
        }
    }

    private val trackPath = Path()
}

private class SliderOffset(
    contentSize: Size,
    private val contentPadding: Size
) {
    private val contentWidthWithPadding = contentSize.width.minus(2 * contentPadding.width)

    fun getLabelOffset(labelType: LabelType): Offset {

        val labelOffset = when (labelType) {
            LabelType.Start -> 0f
            is LabelType.End -> contentWidthWithPadding - labelType.measuredText.size.width
        }

        return Offset(
            x = contentPadding.width + labelOffset,
            y = contentPadding.height
        )
    }
}

private sealed interface LabelType {
    data object Start : LabelType

    class End(val measuredText: TextLayoutResult) : LabelType
}

private fun stepsToTickFractions(steps: Int): FloatArray {
    return if (steps == 0)
        floatArrayOf()
    else
        FloatArray(steps + 2) { it.toFloat() / (steps + 1) }
}

private fun calcFraction(range: ClosedFloatingPointRange<Float>, position: Float): Float {
    val value = if (range.endInclusive - range.start == 0f)
        0f
    else
        (position - range.start) / (range.endInclusive - range.start)

    return value.coerceIn(0f, 1f)
}

private fun DrawScope.drawSliderLabels(
    textMeasurer: TextMeasurer,
    sliderOffset: SliderOffset,
    startLabel: String,
    endLabel: String
) {
    val labelStyle = MNXTypography.labelMedium.copy(
        color = HalfTransparentWhite,
        textAlign = TextAlign.Center
    )

    val startLabelMeasuredText = textMeasurer.measure(
        text = startLabel,
        style = labelStyle
    )

    val startLabelOffset = sliderOffset.getLabelOffset(labelType = LabelType.Start)

    drawText(
        textLayoutResult = startLabelMeasuredText,
        topLeft = startLabelOffset
    )

    val endLabelMeasuredText = textMeasurer.measure(
        text = endLabel,
        style = labelStyle
    )

    val endLabelOffset = sliderOffset.getLabelOffset(
        labelType = LabelType.End(measuredText = endLabelMeasuredText)
    )

    drawText(
        textLayoutResult = endLabelMeasuredText,
        topLeft = endLabelOffset
    )
}

@Preview
@Composable
private fun MNXSliderPreview() {
    MNXTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val number = remember {
                mutableFloatStateOf(2500f)
            }

            Text(
                text = "Value = ${number.floatValue}",
                color = MaterialTheme.colorScheme.onPrimary
            )

            MNXSlider(
                value = number.floatValue,
                onValueChange = { number.floatValue = it },
                valueRange = 800f..3000f,
                label = { "${it.toInt()} W" }
            )
        }
    }
}

@Preview
@Composable
private fun MNXRangeSliderPreview() {
    MNXTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val numberRange = remember {
                mutableStateOf(1300f..2200f)
            }

            Text(
                text = "Value = ${numberRange.value}",
                color = MaterialTheme.colorScheme.onPrimary
            )

            MNXRangeSlider(
                value = numberRange.value,
                onValueChange = { numberRange.value = it },
                valueRange = 800f..3000f
            )
        }
    }
}