package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun RoundTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = AppTheme.colors.textPrimary,
        backgroundColor = AppTheme.colors.background,
        disabledTextColor = Color.Transparent,
        cursorColor = AppTheme.colors.textPrimary,
        focusedBorderColor = AppTheme.colors.border,
        unfocusedBorderColor = AppTheme.colors.border,
    ),
    shouldMask: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        colors = colors,
        singleLine = true,
        modifier = modifier,
        onValueChange = { newValue -> onValueChange(newValue) },
        shape = RoundedCornerShape(50),
        placeholder = { Text(text = placeholder, color = AppTheme.colors.textHint) },
        visualTransformation = if (shouldMask) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview(showBackground = true)
@Composable
fun RoundTextFieldMaskedPreview() =
    RoundTextField(
        value = "password",
        onValueChange = {},
        colors = TextFieldDefaults.textFieldColors(),
        shouldMask = true
    )

@Preview(showBackground = true)
@Composable
fun RoundTextFieldUnMaskedPreview() =
    RoundTextField(
        value = "",
        onValueChange = {},
        placeholder = "email",
        colors = TextFieldDefaults.textFieldColors(),
        shouldMask = false
    )
