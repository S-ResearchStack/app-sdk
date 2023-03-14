package healthstack.kit.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun LabeledCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    labelText: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 10.dp)
    ) {
        Checkbox(
            modifier = Modifier
                .padding(end = 10.dp)
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
            style = AppTheme.typography.subtitle3,
            color = AppTheme.colors.onSurface,
        )
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun LabeledCheckBoxUnCheckedPreview() =
    LabeledCheckbox(false, nothing, "un-checked")

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun LabeledCheckBoxCheckedPreview() =
    LabeledCheckbox(true, nothing, "checked")

private val nothing: (Boolean) -> Unit = { }
