package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.BorderStroke
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
    textColor: Color = AppTheme.colors.background,
    border: BorderStroke? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
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
