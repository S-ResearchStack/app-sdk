package researchstack.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import researchstack.presentation.theme.AppTheme

@Composable
fun Waveform(
    modifier: Modifier = Modifier,
    spikeColor: Color = AppTheme.colors.primary,
    spikeWidth: Dp = 1.dp,
    spikePadding: Dp = 1.dp,
    axisColor: Color = AppTheme.colors.primaryVariant,
    axisHeight: Dp = 2.dp,
    amplitudes: List<Int> = listOf(),
    move: Boolean = false,
) {
    val perSpike = remember(spikeWidth, spikePadding) { spikeWidth + spikePadding }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .then(modifier),
    ) {
        drawRect(
            color = axisColor,
            topLeft = Offset(
                x = 0F,
                y = size.height / 2 - axisHeight.toPx() / 2
            ),
            size = Size(
                width = size.width,
                height = axisHeight.toPx()
            )
        )
        val maxSpikes = (size.width / perSpike.toPx()).toInt()
        val toDraw = amplitudes.takeLast(maxSpikes)
        val start = if (move) size.width - ((toDraw.size) * perSpike.toPx()) else
            size.width - ((toDraw.size) * perSpike.toPx()) + spikePadding.toPx()
        toDraw.forEachIndexed { index, amplitude ->
            val normalizedAmp = (amplitude / 70)
                .coerceAtMost(size.height.toInt())
                .coerceAtLeast(axisHeight.toPx().toInt())
                .toFloat()
            drawRect(
                color = spikeColor,
                topLeft = Offset(
                    x = start + index * perSpike.toPx(),
                    y = size.height / 2 - normalizedAmp / 2
                ),
                size = Size(
                    width = spikeWidth.toPx(),
                    height = normalizedAmp
                ),
                alpha = 0.6F,
            )
        }
    }
}
