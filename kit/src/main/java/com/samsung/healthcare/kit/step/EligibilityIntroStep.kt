package com.samsung.healthcare.kit.step

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.EligibilityIntroModel
import com.samsung.healthcare.kit.view.EligibilityIntroView
import com.samsung.healthcare.kit.view.View

class EligibilityIntroStep(
    id: String,
    name: String,
    model: EligibilityIntroModel,
    view: View<EligibilityIntroModel> = EligibilityIntroView(),
) : Step<EligibilityIntroModel, Boolean>(id, name, model, view, { true }) {

    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
