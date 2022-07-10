package com.samsung.healthcare.kit.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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

@Preview(showBackground = true)
@Composable
fun EligibilityResultViewSuccessPreview() {
    val view = EligibilityResultView()
    val model = EligibilityResultModel(
        id = "eligibility",
        title = "Eligibility Results",
        successModel = ImageArticleModel(
            id = "eligibility",
            title = "Great, You’re in!",
            description = "Congratulations! You are eligible for the study. Next, we will need to collect your consent, and you will be ready to go.",
            drawableId = R.drawable.sample_image_alpha1
        ),
        failModel = ImageArticleModel(
            id = "eligibility",
            title = "You’re not eligible for the study.",
            description = "Please check back later and stay tuned for more studies coming soon!",
            drawableId = R.drawable.sample_image_alpha1
        )
    )
    val callbackCollection = object : CallbackCollection() {
        override fun getEligibility(): Boolean = true
    }

    return view.Render(
        model,
        callbackCollection,
        null
    )
}

@Preview(showBackground = true)
@Composable
fun EligibilityResultViewFailPreview() {
    val view = EligibilityResultView()
    val model = EligibilityResultModel(
        id = "eligibility",
        title = "Eligibility Results",
        successModel = ImageArticleModel(
            id = "eligibility",
            title = "Great, You’re in!",
            description = "Congratulations! You are eligible for the study. Next, we will need to collect your consent, and you will be ready to go.",
            drawableId = R.drawable.sample_image_alpha1
        ),
        failModel = ImageArticleModel(
            id = "eligibility",
            title = "You’re not eligible for the study.",
            description = "Please check back later and stay tuned for more studies coming soon!",
            drawableId = R.drawable.sample_image_alpha1
        )
    )

    val callbackCollection = object : CallbackCollection() {
        override fun getEligibility(): Boolean = false
    }

    return view.Render(
        model,
        callbackCollection,
        null
    )
}
