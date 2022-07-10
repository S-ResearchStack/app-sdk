package com.samsung.healthcare.kit.view.auth

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
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.auth.SignInProvider
import com.samsung.healthcare.kit.auth.SignInProvider.Basic
import com.samsung.healthcare.kit.auth.SignInProvider.Google
import com.samsung.healthcare.kit.view.common.SquareButton
import com.samsung.healthcare.kit.view.common.SquareTextField

object SignUpComponent {
    @Composable
    fun of(provider: SignInProvider): @Composable (() -> Unit) -> Unit =
        when (provider) {
            Google -> { onClick -> GoogleSignInButton(onClick) }
            Basic -> { onClick -> BasicSignUpComponent(onClick) }
        }
}

@Composable
fun BasicSignUpComponent(onClick: () -> Unit) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val passwordConfirmState = remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SquareTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
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
                .width(312.dp)
                .height(48.dp),
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            placeholder = "Password",
            shouldMask = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        SquareTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .width(312.dp)
                .height(48.dp),
            value = passwordConfirmState.value,
            onValueChange = { passwordConfirmState.value = it },
            placeholder = "Confirm Password",
            shouldMask = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        SquareButton(
            modifier = Modifier
                .width(320.dp)
                .height(44.dp),
            text = "Sign Up",
            onClick = onClick,
            enabled = emailState.value != ""
        )
    }
}
