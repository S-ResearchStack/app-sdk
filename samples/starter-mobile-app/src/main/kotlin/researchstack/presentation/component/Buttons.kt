package researchstack.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.LocalTypography

@Suppress("LongParameterList")
@Composable
fun AppTextButton(
    text: String,
    textColor: Color = AppTheme.colors.surface,
    backgroundColor: Color = AppTheme.colors.primary,
    borderColor: Color = AppTheme.colors.primary,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(size = 4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = backgroundColor.copy(alpha = 0.2f),
        ),
        border = borderStroke(enabled, borderColor),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
    ) {
        Text(
            text = text,
            color = textColor,
            style = LocalTypography.current.title2
        )
    }
}

@Composable
private fun borderStroke(enabled: Boolean, borderColor: Color) = if (enabled) BorderStroke(
    ButtonDefaults.OutlinedBorderSize, borderColor
) else null
