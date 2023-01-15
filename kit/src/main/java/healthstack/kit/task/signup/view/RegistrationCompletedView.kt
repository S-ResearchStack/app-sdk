package healthstack.kit.task.signup.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.task.base.View
import healthstack.kit.task.signup.model.RegistrationCompletedModel
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.ui.layout.ImageArticleLayout

class RegistrationCompletedView : View<RegistrationCompletedModel>() {
    @Composable
    override fun Render(
        model: RegistrationCompletedModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val imageArticleModel: ImageArticleModel = model.toImageArticleModel()

        ImageArticleLayout(
            "Registration Completed",
            imageArticleModel,
            model.buttonText,
            onComplete = { callbackCollection.next() },
            onClickBack = { callbackCollection.prev() }
        )
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun RegistrationCompletedViewPreview() =
    RegistrationCompletedView().Render(
        RegistrationCompletedModel(
            "id",
            "You are done!",
            "Continue",
            "Congratulations! Everything is all set for you. Now please tap on the button below to start your journey!",
            R.drawable.sample_image_alpha1
        ),
        CallbackCollection(),
        null
    )
