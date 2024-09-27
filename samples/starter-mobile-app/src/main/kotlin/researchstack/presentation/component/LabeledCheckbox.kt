package researchstack.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import researchstack.presentation.theme.AppTheme

@Suppress("MagicNumber")
@Composable
fun LabeledCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    labelText: String,
    style: TextStyle = AppTheme.typography.body1,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onCheckedChange(!isChecked) }
    ) {
        Checkbox(
            modifier = Modifier
                .absoluteOffset(-(12.dp), 0.dp)
                .testTag(labelText),
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = AppTheme.colors.primary,
                uncheckedColor = AppTheme.colors.primary.copy(0.38F),
                checkmarkColor = AppTheme.colors.surface,
                disabledColor = AppTheme.colors.disabled
            )
        )
        Text(
            text = labelText,
            style = style,
            color = AppTheme.colors.onSurface,
        )
    }
}
