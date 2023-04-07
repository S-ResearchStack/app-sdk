package healthstack.kit.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.util.InteractionType
import healthstack.kit.ui.util.InteractionUtil
import kotlinx.coroutines.delay

@Composable
fun CircularTimer(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.primary,
    strokeWidth: Dp = 24.dp,
    timeSeconds: Long = 10,
    interactionType: InteractionType = InteractionType.NOTHING,
    callback: () -> Unit = {},
) {
    val leftTimeSeconds = remember { mutableStateOf(timeSeconds) }
    val composeContext = LocalContext.current

    LaunchedEffect(timeSeconds > 0) {
        while (leftTimeSeconds.value > 0) {
            delay(1000)
            --leftTimeSeconds.value
        }

        InteractionUtil.feedback(composeContext, interactionType)
        callback()
    }

    val progress = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = (timeSeconds * 1000).toInt(),
                easing = LinearEasing
            )
        )
    }

    Box(
        modifier = modifier
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .size(250.dp),
            color = color
                .copy(alpha = 0.2f),
            progress = 1f,
            strokeWidth = strokeWidth,
        )

        CircularProgressIndicator(
            modifier = modifier
                .size(250.dp),
            color = color,
            progress = progress.value,
            strokeWidth = strokeWidth,
        )

        Text(
            text = formatTime(leftTimeSeconds.value),
            style = AppTheme.typography.headline1,
            color = AppTheme.colors.primary,
            textAlign = TextAlign.Center,
        )
    }
}

private fun formatTime(timeSeconds: Long): String {
    return String.format("%02d", (timeSeconds / 60) % 60) + ":" + String.format("%02d", timeSeconds % 60)
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun CircularTimerPreview() {
    val color = AppTheme.colors.primary
    val strokeWidth = 24.dp

    Box(
        modifier = Modifier
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(250.dp),
            color = color
                .copy(alpha = 0.2f),
            progress = 1f,
            strokeWidth = strokeWidth,
        )

        CircularProgressIndicator(
            modifier = Modifier
                .size(250.dp),
            color = color,
            progress = 0.2f,
            strokeWidth = strokeWidth,
        )

        Text(
            text = "00:05",
            style = AppTheme.typography.headline1,
            color = AppTheme.colors.primary,
            textAlign = TextAlign.Center,
        )
    }
}
