package com.samsung.healthcare.kit.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.ImageArticleModel
import com.samsung.healthcare.kit.model.RegistrationCompletedModel
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.view.layout.ImageArticleLayout

class RegistrationCompletedView() : View<RegistrationCompletedModel>() {
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
            onComplete = { callbackCollection.next() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationCompletedViewPreview() =
    RegistrationCompletedView().Render(
        RegistrationCompletedModel(
            "id",
            "You are done!",
            "Continue",
            "Congratulations! Everything is all set for you. Now please tap on the button below to start your journey!",
            R.drawable.sample_image2
        ),
        CallbackCollection(),
        null
    )
