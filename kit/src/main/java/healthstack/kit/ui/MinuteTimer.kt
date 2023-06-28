package healthstack.kit.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun MinuteTextTimer(
    timeSeconds: Int = 10,
    onTimeout: () -> Unit = {},
) {
    val leftTimeSeconds = remember { mutableStateOf(timeSeconds) }
    LaunchedEffect(Unit) {
        while (leftTimeSeconds.value > 0) {
            delay(1000)
            --leftTimeSeconds.value
        }

        onTimeout()
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 47.dp),
        text = formatTime(leftTimeSeconds.value),
        style = AppTheme.typography.headline3,
        color = AppTheme.colors.onSurface,
        textAlign = TextAlign.Center,
    )
}

private fun formatTime(timeSeconds: Int): String {
    return String.format("%02d", (timeSeconds / 60) % 60) + ":" + String.format("%02d", timeSeconds % 60)
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun MinuteTimerPreview() {
    MinuteTextTimer()
}
