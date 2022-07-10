package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun LabeledCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    labelText: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Checkbox(
            modifier = Modifier
                .padding(end = 10.dp)
                .testTag("checkBox"),
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = AppTheme.colors.primary,
                uncheckedColor = AppTheme.colors.primaryVariant,
                checkmarkColor = AppTheme.colors.surface,
                disabledColor = Color(0xFFA1A1A1)
            )
        )
        Text(
            text = labelText,
            style = AppTheme.typography.body1,
            color = AppTheme.colors.textPrimary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LabeledCheckBoxUnCheckedPreview() =
    LabeledCheckbox(false, {}, "un-checked")

@Preview(showBackground = true)
@Composable
fun LabeledCheckBoxCheckedPreview() =
    LabeledCheckbox(true, {}, "checked")
