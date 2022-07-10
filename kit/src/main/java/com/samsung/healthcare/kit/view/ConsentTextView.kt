package com.samsung.healthcare.kit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.samsung.healthcare.kit.R.string
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.ConsentTextModel
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.view.layout.ConsentTextLayout
import com.samsung.healthcare.kit.view.layout.SignatureLayout

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
