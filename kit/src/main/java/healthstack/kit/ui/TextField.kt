package healthstack.kit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

@Composable
fun RoundTextBoxWithIcon(
    label: String = "",
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "Select",
    enabled: Boolean = true,
    icon: ImageVector = Icons.Filled.AccessTimeFilled,
    onClick: () -> Unit = { },
) {
    Column(
        modifier = modifier.height(70.dp)
    ) {
        Text(
            text = label,
            style = AppTheme.typography.body3,
            maxLines = 1,
            color = AppTheme.colors.onSurface.copy(0.6F)
        )

        Spacer(Modifier.height(6.dp))

        OutlinedButton(
            onClick = onClick,
            border = if (value.isEmpty() || !enabled)
                BorderStroke(1.dp, AppTheme.colors.primaryVariant)
            else
                BorderStroke(1.dp, AppTheme.colors.primary),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (value.isEmpty() || !enabled) AppTheme.colors.onSurface.copy(0.6F)
                else AppTheme.colors.onSurface
            ),
            modifier = modifier.height(48.dp)
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (value.isEmpty())
                    Text(
                        text = placeholder,
                        style = AppTheme.typography.body1,
                    )
                else
                    Text(
                        text = value,
                        style = AppTheme.typography.body1,
                    )

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (enabled) AppTheme.colors.primary else AppTheme.colors.primaryVariant
                )
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun RoundTextBoxWithIconPreview() {
    RoundTextBoxWithIcon(
        value = "",
    )
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun RoundTextBoxWithIconPreview2() {
    RoundTextBoxWithIcon(
        value = "Value2",
    )
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun RoundTextBoxWithIconPreview3() {
    RoundTextBoxWithIcon(
        enabled = false,
        value = "Value2",
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
