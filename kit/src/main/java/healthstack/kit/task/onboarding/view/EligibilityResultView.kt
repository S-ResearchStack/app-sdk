package healthstack.kit.task.onboarding.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.R
import healthstack.kit.R.drawable
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.task.base.View
import healthstack.kit.task.onboarding.model.EligibilityResultModel
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.ui.ButtonShape.ROUND
import healthstack.kit.ui.layout.ImageArticleLayout

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
            buttonHidden = callbackCollection.getEligibility().not(),
            buttonShape = ROUND
        )
    }
}

@PreviewGenerated
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
            description = "Congratulations! You are eligible for the study. " +
                "Next, we will need to collect your consent, and you will be ready to go.",
            drawableId = drawable.sample_image_alpha1
        ),
        failModel = ImageArticleModel(
            id = "eligibility",
            title = "You’re not eligible for the study.",
            description = "Please check back later and stay tuned for more studies coming soon!",
            drawableId = drawable.sample_image_alpha1
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

@PreviewGenerated
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
            description = "Congratulations! You are eligible for the study. " +
                "Next, we will need to collect your consent, and you will be ready to go.",
            drawableId = drawable.sample_image_alpha1
        ),
        failModel = ImageArticleModel(
            id = "eligibility",
            title = "You’re not eligible for the study.",
            description = "Please check back later and stay tuned for more studies coming soon!",
            drawableId = drawable.sample_image_alpha1
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
