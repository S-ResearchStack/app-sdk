package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun RoundButton(
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
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            disabledBackgroundColor = Color(0xFFB3C6F1)
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        border = border,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = textColor,
        )
    }
}

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
    Button(
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            disabledBackgroundColor = Color(0xFFB3C6F1)
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        border = border,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = textColor,
            style = AppTheme.typography.subHeader2
        )
    }
}

enum class ButtonShape {
    SQUARE, ROUND
}
