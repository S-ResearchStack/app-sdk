package com.samsung.healthcare.kit.view.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.question.QuestionModel
import com.samsung.healthcare.kit.model.question.QuestionModel.QuestionType
import com.samsung.healthcare.kit.theme.AppTheme

abstract class QuestionComponent<T : QuestionModel<*>> : Component<T>() {
    @Composable
    override fun Render(model: T, callbackCollection: CallbackCollection) {
        Text(
            text = model.title,
            style = AppTheme.typography.subHeader2,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
        )

        if (!model.explanation.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = model.explanation,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 12.dp, end = 12.dp),
                style = AppTheme.typography.body2,
                color = AppTheme.colors.textHint
            )
        }
    }

    companion object {
        fun defaultComponentOf(type: QuestionType): Component<out QuestionModel<*>> =
            when (type) {
                QuestionType.Choice -> ChoiceQuestionComponent()
                QuestionType.Text -> TextInputQuestionComponent()
                QuestionType.MultipleChoice -> MultiChoiceQuestionComponent()
                else -> TODO()
            }
    }
}
