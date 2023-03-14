package healthstack.kit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme

enum class ButtonShape {
    SQUARE, ROUND
}

@Composable
fun KitButton(
    text: String = "",
    buttonColor: Color = AppTheme.colors.primary,
    textColor: Color = AppTheme.colors.surface,
    shape: RoundedCornerShape = RoundedCornerShape(
        MaterialTheme.shapes.small.topStart,
        MaterialTheme.shapes.small.topEnd,
        MaterialTheme.shapes.small.bottomEnd,
        MaterialTheme.shapes.small.bottomStart,
    ),
    border: BorderStroke? = null,
    modifier: Modifier = Modifier
        .height(44.dp)
        .fillMaxWidth(),
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            disabledBackgroundColor = buttonColor.copy(alpha = 0.33F)
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        border = border,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = textColor,
            style = AppTheme.typography.title2
        )
    }
}
