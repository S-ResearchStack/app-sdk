package healthstack.kit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun ToggleSwitch(text: String, initialState: Boolean, changeState: (Boolean) -> Unit = {}) {
    var enable by remember { mutableStateOf(initialState) }

    Row(
        Modifier
            .fillMaxWidth()
            .height(26.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = AppTheme.typography.body1,
            color = AppTheme.colors.onBackground,
        )

        Switch(
            checked = initialState,
            onCheckedChange = {
                enable = !initialState
                changeState(enable)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.primary,
                checkedTrackColor = AppTheme.colors.primary,
                checkedTrackAlpha = 0.38F,
                uncheckedThumbColor = AppTheme.colors.surface,
                uncheckedTrackColor = AppTheme.colors.onSurface,
                uncheckedTrackAlpha = 0.08F
            ),
            modifier = Modifier
                .testTag("toggle")
                .width(48.dp)
                .height(24.dp)
                .padding(0.dp)
        )
    }
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun ToggleSwitchPreview() = ToggleSwitch("Push", false)

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun ToggleSwitchPreview2() = ToggleSwitch("Push", true)
