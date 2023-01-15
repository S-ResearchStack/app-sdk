package healthstack.sample.registration

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
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.sample.registration.RegistrationState.Failed
import healthstack.sample.registration.RegistrationState.Init
import healthstack.sample.registration.RegistrationState.Loading
import healthstack.sample.registration.RegistrationState.Success

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
