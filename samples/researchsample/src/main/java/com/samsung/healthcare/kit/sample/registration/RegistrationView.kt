package com.samsung.healthcare.kit.sample.registration

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.question.QuestionModel
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Failed
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Init
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Loading
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Success
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.View
import com.samsung.healthcare.kit.view.common.BottomRoundButton
import com.samsung.healthcare.kit.view.common.TopBar
import com.samsung.healthcare.kit.view.component.Component
import com.samsung.healthcare.kit.view.component.QuestionComponent.Companion.defaultComponentOf

class RegistrationView(
    private val viewModel: RegistrationViewModel = RegistrationViewModel()
) : View<RegistrationModel>() {

    @Composable
    override fun Render(
        model: RegistrationModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?
    ) {
        val state = viewModel.state.collectAsState()

        when (state.value) {
            Init, Failed -> RegistrationShow(model, callbackCollection)
            Loading -> ProgressIndicator()
            Success -> callbackCollection.next()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun RegistrationShow(
        model: RegistrationModel,
        callbackCollection: CallbackCollection
    ) {
        val scrollSate = rememberScrollState()
        Scaffold(
            topBar = { TopBar(title = model.title) },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollSate)
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                model.questions.forEach {
                    (defaultComponentOf(it.type) as Component<QuestionModel<*>>).Render(
                        model = it,
                        callbackCollection = callbackCollection
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                BottomRoundButton(text = "Submit") {
                    if (model.questions.all { it.getResponse() != null }) {
                        viewModel.registerUser(model.questions.associate { it.id to it.getResponse()!! })
                    }
                }
            }
        }
    }

    @Composable
    private fun ProgressIndicator() {
        val focusManager = LocalFocusManager.current
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(AppTheme.colors.background)
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
            )
        }
    }
}
