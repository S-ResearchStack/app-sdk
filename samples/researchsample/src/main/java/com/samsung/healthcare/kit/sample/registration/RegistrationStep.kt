package com.samsung.healthcare.kit.sample.registration

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.step.Step
import com.samsung.healthcare.kit.view.View

class RegistrationStep(
    model: RegistrationModel,
    view: View<RegistrationModel> = RegistrationView(),
) : Step<RegistrationModel, Boolean>("", "", model, view, { true }) {

    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model = model, callbackCollection = callbackCollection, holder = null)
    }
}
