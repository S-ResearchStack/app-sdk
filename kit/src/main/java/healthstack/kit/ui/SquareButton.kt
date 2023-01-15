package healthstack.kit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme

@Composable
fun SquareButton(
    text: String = "",
    buttonColor: Color = AppTheme.colors.primary,
    textColor: Color = AppTheme.colors.textSecondary,
    border: BorderStroke? = null,
    modifier: Modifier = Modifier
        .height(44.dp)
        .width(320.dp),
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    KitButton(
        text,
        buttonColor,
        textColor,
        RoundedCornerShape(0),
        border,
        modifier,
        enabled,
        onClick
    )
}
