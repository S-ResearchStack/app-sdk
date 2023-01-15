package healthstack.kit.task.onboarding.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R.string
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.onboarding.model.ConsentTextModel
import healthstack.kit.task.onboarding.view.layout.ConsentTextLayout
import healthstack.kit.task.onboarding.view.layout.SignatureLayout
import healthstack.kit.task.survey.question.SubStepHolder

class ConsentTextView(
    private val buttonText: String? = null,
) : View<ConsentTextModel>() {
    @Composable
    override fun Render(
        model: ConsentTextModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        var mutableSvg by rememberSaveable { mutableStateOf(model.encodedSignature) }
        var signaturePadVisible by rememberSaveable { mutableStateOf(false) }

        val joinButtonText = buttonText ?: LocalContext.current.getString(string.join_study)

        ConsentTextLayout(
            mutableSvg,
            model,
            joinButtonText,
            callbackCollection
        ) {
            signaturePadVisible = true
        }

        if (signaturePadVisible)
            SignatureLayout(
                onClickDone = { svg ->
                    mutableSvg = svg
                    model.encodedSignature = svg
                    signaturePadVisible = false
                },
                onClickCancel = { signaturePadVisible = false }
            )
    }
}
