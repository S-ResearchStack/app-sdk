package com.samsung.healthcare.kit.step.sub

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.question.QuestionModel
import com.samsung.healthcare.kit.view.component.Component

class QuestionSubStep<T : QuestionModel<R>, R>(
    val id: String,
    val name: String,
    val model: T,
    private val component: Component<T>, // @Composable (T, () -> Unit) -> Unit,
) {
    @Composable
    fun Render(callbackCollection: CallbackCollection) {
        component.Render(model, callbackCollection)
    }

    fun getState(): T = model

    fun getResult(): R? = model.getResponse()
}
