package healthstack.kit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme

@Composable
fun RoundButton(
    text: String = "",
    buttonColor: Color = AppTheme.colors.primary,
    textColor: Color = AppTheme.colors.surface,
    border: BorderStroke? = null,
    modifier: Modifier = Modifier
        .height(44.dp)
        .fillMaxWidth(),
    radius: Dp = 4.dp,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    KitButton(
        text,
        buttonColor,
        textColor,
        RoundedCornerShape(radius),
        border,
        modifier,
        enabled,
        onClick
    )
}
