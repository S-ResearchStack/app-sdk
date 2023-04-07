package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.TextInputQuestionModel
import healthstack.kit.theme.AppTheme

class TextInputQuestionComponent : QuestionComponent<TextInputQuestionModel>() {

    @Composable
    override fun Render(model: TextInputQuestionModel, callbackCollection: CallbackCollection) {
        Column {
            super.Render(model, callbackCollection)

            Spacer(modifier = Modifier.height(29.dp))

            var response by remember { mutableStateOf(model.getResponse()) }

            InputTextField(
                response,
                Modifier
                    .fillMaxWidth()
                    .testTag("TextQuestionInputField")
            ) {
                response = it
                model.input = it
            }
        }
    }

    @Composable
    private fun InputTextField(text: String, modifier: Modifier, onValueChange: (String) -> Unit) {
        TextField(
            value = text,
            modifier = modifier,
            placeholder = { Text("Type here..") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = AppTheme.colors.onSurface,
                backgroundColor = AppTheme.colors.background,
                unfocusedIndicatorColor = AppTheme.colors.primary
            ),
            singleLine = true,
            onValueChange = { onValueChange(it) },
        )
    }
}
