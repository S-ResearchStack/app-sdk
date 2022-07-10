package com.samsung.healthcare.kit.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.EligibilityResultModel
import com.samsung.healthcare.kit.model.ImageArticleModel
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.view.layout.ImageArticleLayout

class EligibilityResultView : View<EligibilityResultModel>() {
    @Composable
    override fun Render(
        model: EligibilityResultModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val resultImageArticleModel: ImageArticleModel =
            if (callbackCollection.getEligibility()) model.successModel else model.failModel

        ImageArticleLayout(
            model.title,
            resultImageArticleModel,
            LocalContext.current.getString(R.string.continuous),
            onClickBack = { callbackCollection.prev() },
            onComplete = { callbackCollection.next() },
            buttonHidden = callbackCollection.getEligibility().not()
        )
    }
}
