package com.samsung.healthcare.kit.step

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.SignUpModel
import com.samsung.healthcare.kit.view.SignUpView
import com.samsung.healthcare.kit.view.View

class SignUpStep(
    id: String,
    name: String,
    model: SignUpModel,
    view: View<SignUpModel> = SignUpView(),
) : Step<SignUpModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model, callbackCollection, null)
    }
}
