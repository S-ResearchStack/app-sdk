package com.samsung.healthcare.kit.view.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.auth.SignInProvider
import com.samsung.healthcare.kit.auth.SignInProvider.Basic
import com.samsung.healthcare.kit.auth.SignInProvider.Google
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.common.RoundButton
import com.samsung.healthcare.kit.view.common.RoundTextField

object SignUpComponent {
    fun of(provider: SignInProvider): @Composable (CallbackCollection) -> Unit =
        when (provider) {
            Google -> { callbackCollection -> GoogleSignInButton(callbackCollection) }
            Basic -> { callbackCollection -> BasicSignUpComponent(callbackCollection) }
        }
}

@Composable
fun BasicSignUpComponent(callbackCollection: CallbackCollection) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val passwordConfirmState = remember { mutableStateOf("") }
    RoundTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        value = emailState.value,
        onValueChange = { emailState.value = it },
        placeholder = "Email Address",
    )

    Spacer(modifier = Modifier.height(10.dp))

    RoundTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        placeholder = "Password",
        shouldMask = true,
    )

    Spacer(modifier = Modifier.height(10.dp))

    RoundTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        value = passwordConfirmState.value,
        onValueChange = { passwordConfirmState.value = it },
        placeholder = "Confirm Password",
        shouldMask = true,
    )

    Spacer(modifier = Modifier.height(20.dp))

    RoundButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        text = "Sign Up",
        textColor = AppTheme.colors.textPrimary,
        onClick = {
            // TODO how to handle sign up with external
            callbackCollection.next()
        }
    )
}
