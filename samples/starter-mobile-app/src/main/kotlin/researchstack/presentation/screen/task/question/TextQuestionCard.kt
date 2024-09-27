package researchstack.presentation.screen.task.question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import researchstack.domain.model.task.question.TextQuestion
import researchstack.presentation.theme.AppTheme

@Composable
@Suppress("UNUSED_PARAMETER")
fun TextQuestionCard(question: TextQuestion, onChangedResult: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    Column {
        OutlinedTextField(
            value = value,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("TextQuestionInputField"),
            textStyle = AppTheme.typography.body2,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = AppTheme.colors.onSurface,
                disabledTextColor = AppTheme.colors.disabled,
                backgroundColor = AppTheme.colors.background,
                cursorColor = AppTheme.colors.primary,
                errorCursorColor = AppTheme.colors.error,
                focusedBorderColor = AppTheme.colors.primary,
                unfocusedBorderColor = AppTheme.colors.primaryVariant,
                disabledBorderColor = AppTheme.colors.disabled,
                errorBorderColor = AppTheme.colors.error,
            ),
            singleLine = true,
            onValueChange = {
                value = it
                onChangedResult(value)
            },
        )
        /*
        Spacer(Modifier.height(2.dp))
        Text(
            text = "${value.length} / ${question.maxCharacters}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Right,
            color = AppTheme.colors.onBackground.copy(0.6F),
            style = AppTheme.typography.overline1
        )
         */
    }
}

/*private fun calculateHeight(question: TextQuestion): Dp =
    if (question.isSingleLine)
        50.dp
    else 152.dp*/
