package com.samsung.healthcare.kit.sample.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Failed
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Init
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Loading
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Success
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.View

class RegistrationView(
    private val viewModel: RegistrationViewModel = RegistrationViewModel(),
) : View<RegistrationModel>() {

    @Composable
    override fun Render(
        model: RegistrationModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val state = viewModel.state.collectAsState()
        when (state.value) {
            Init, Failed -> registerUser(model)
            Loading -> ProgressIndicator()
            Success -> callbackCollection.next()
        }
    }

    private fun registerUser(model: RegistrationModel) {
        val profiles = model.eligibilityQuestions.associate { it.id to it.getResponse() }
            .filter { it.value != null } as Map<String, Any>
        viewModel.registerUser(profiles)
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
