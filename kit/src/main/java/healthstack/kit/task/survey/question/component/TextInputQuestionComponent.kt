package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.TextInputQuestionModel
import healthstack.kit.theme.AppTheme

class TextInputQuestionComponent : QuestionComponent<TextInputQuestionModel>() {

    @Composable
    override fun Render(model: TextInputQuestionModel, callbackCollection: CallbackCollection) {
        Column {
            super.Render(model, callbackCollection)

            Spacer(modifier = Modifier.height(20.dp))

            InputTextField(
                model
            )
        }
    }

    @Composable
    fun InputTextField(model: TextInputQuestionModel) {

        var value by remember { mutableStateOf(model.input) }

        Column(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                modifier = Modifier.fillMaxWidth().height(152.dp)
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
                singleLine = false,
                onValueChange = {
                    if (it.length <= model.maxCharacter) {
                        value = it
                        model.input = it
                    }
                },
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${value.length} / ${model.maxCharacter}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right,
                color = AppTheme.colors.onBackground.copy(0.6F),
                style = AppTheme.typography.overline1
            )
        }
    }

    @PreviewGenerated
    @Preview(showBackground = true)
    @Composable
    private fun TextInputQuestionComponentPreview() =
        TextInputQuestionComponent().Render(
            TextInputQuestionModel(
                "id",
                "Have you experienced any chest pains during exercise? If so, please describe the symptoms in context.",
                "explanation",
            ),
            CallbackCollection()
        )
}
