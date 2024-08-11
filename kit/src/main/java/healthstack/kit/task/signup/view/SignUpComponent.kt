package healthstack.kit.task.signup.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import healthstack.kit.auth.SignInProvider
import healthstack.kit.auth.SignInProvider.Google
import healthstack.kit.ui.SquareButton
import healthstack.kit.ui.SquareTextField

object SignUpComponent {
    @Composable
    fun of(provider: SignInProvider): @Composable (() -> Unit) -> Unit =
        when (provider) {
            Google -> { onClick -> GoogleSignInButton(onClick) }
            else -> {_ -> Unit}
        }

    @Composable
    fun ofBasic(): @Composable ((email: String, password: String) -> Unit) -> Unit =
        { onClick -> BasicSignUpComponent(onClick) }
}

internal const val EMAIL_TEST_TAG = "emailTestTag"
internal const val PASSWORD_TEST_TAG = "passwordTag"
internal const val SIGNUP_BUTTON_TEST_TAG = "signupButtonTag"

@Composable
fun BasicSignUpComponent(onClick: (email: String, password: String) -> Unit) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SquareTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .testTag(EMAIL_TEST_TAG)
                .width(312.dp)
                .height(48.dp),
            value = emailState.value,
            onValueChange = { emailState.value = it },
            placeholder = "Email Address",
        )

        Spacer(modifier = Modifier.height(10.dp))

        SquareTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .testTag(PASSWORD_TEST_TAG)
                .width(312.dp)
                .height(48.dp),
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            placeholder = "Password",
            shouldMask = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        SquareButton(
            modifier = Modifier
                .testTag(SIGNUP_BUTTON_TEST_TAG)
                .width(320.dp)
                .height(44.dp),
            text = "Sign Up",
            onClick = {
                onClick(emailState.value, passwordState.value)
            },
            enabled = emailState.value != ""
        )
    }
}
