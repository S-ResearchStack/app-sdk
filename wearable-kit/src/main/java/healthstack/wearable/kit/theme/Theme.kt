package healthstack.wearable.kit.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun HealthWearableTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = wearColorPalette,
        typography = Typography,
        content = content,
    )
}
