package com.samsung.healthcare.kit.step

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.IntroModel
import com.samsung.healthcare.kit.view.IntroView
import com.samsung.healthcare.kit.view.View

class IntroStep(
    id: String,
    name: String,
    model: IntroModel,
    // TODO parameter of IntroView will be deleted after apply resources pr
    view: View<IntroModel> = IntroView("Get Started"),
) : Step<IntroModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model, callbackCollection, null)
    }
}
