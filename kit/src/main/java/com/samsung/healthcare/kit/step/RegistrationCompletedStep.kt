package com.samsung.healthcare.kit.step

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.RegistrationCompletedModel
import com.samsung.healthcare.kit.view.RegistrationCompletedView
import com.samsung.healthcare.kit.view.View

class RegistrationCompletedStep(
    id: String,
    name: String,
    model: RegistrationCompletedModel,
    view: View<RegistrationCompletedModel> = RegistrationCompletedView(),
) : Step<RegistrationCompletedModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model, callbackCollection, null)
    }
}
