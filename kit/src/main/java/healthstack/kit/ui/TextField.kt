package healthstack.kit.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun RoundTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = AppTheme.colors.onSurface,
        backgroundColor = AppTheme.colors.surface,
        disabledTextColor = AppTheme.colors.onSurface.copy(0.38F),
        cursorColor = AppTheme.colors.primary,
        focusedBorderColor = AppTheme.colors.primary,
        unfocusedBorderColor = Color.Transparent,
    ),
    shouldMask: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        colors = colors,
        singleLine = true,
        modifier = modifier
            .width(312.dp)
            .height(48.dp),
        onValueChange = { newValue -> onValueChange(newValue) },
        shape = RoundedCornerShape(50),
        placeholder = {
            Text(
                text = placeholder,
                style = AppTheme.typography.subtitle2,
                color = AppTheme.colors.onSurface.copy(0.6F),
            )
        },
        visualTransformation = if (shouldMask) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun SquareTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = AppTheme.colors.onSurface,
        backgroundColor = AppTheme.colors.surface,
        disabledTextColor = AppTheme.colors.onSurface.copy(0.38F),
        cursorColor = AppTheme.colors.primary,
        focusedBorderColor = AppTheme.colors.primary,
        unfocusedBorderColor = Color.Transparent,
    ),
    shouldMask: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        colors = colors,
        singleLine = true,
        modifier = modifier
            .width(312.dp)
            .height(48.dp),
        onValueChange = { newValue -> onValueChange(newValue) },
        placeholder = {
            Text(
                text = placeholder,
                style = AppTheme.typography.subtitle2,
                color = AppTheme.colors.onSurface.copy(0.6F),
            )
        },
        visualTransformation = if (shouldMask) PasswordVisualTransformation() else VisualTransformation.None,
    )
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun RoundTextFieldMaskedPreview() {
    RoundTextField(value = "password", onValueChange = nothing, shouldMask = true)
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun RoundTextFieldUnMaskedPreview() {
    RoundTextField(value = "", onValueChange = nothing, placeholder = "email", shouldMask = false)
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun SquareTextFieldMaskedPreview() {
    SquareTextField(value = "password", onValueChange = nothing, shouldMask = true)
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun SquareTextFieldUnMaskedPreview() {
    SquareTextField(value = "", onValueChange = nothing, placeholder = "email", shouldMask = false)
}

private val nothing: (arg: String) -> Unit = { }
