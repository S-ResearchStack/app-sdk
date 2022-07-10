package com.samsung.healthcare.kit.view

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.auth.AuthContractFactory
import com.samsung.healthcare.kit.auth.SignInProvider
import com.samsung.healthcare.kit.auth.SignInProvider.Basic
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.SignUpModel
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.auth.AuthCallback
import com.samsung.healthcare.kit.view.auth.BasicSignUpComponent
import com.samsung.healthcare.kit.view.auth.SignUpComponent
import com.samsung.healthcare.kit.view.common.TopBar
import com.samsung.healthcare.kit.view.util.ViewUtil

class SignUpView : View<SignUpModel>() {
    @Composable
    override fun Render(
        model: SignUpModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        @StringRes val failedToSignInMessage = R.string.failed_to_signin

        val auth = FirebaseAuth.getInstance()
        val context = LocalContext.current

        val authCallback = createAuthCallback(context, auth, callbackCollection, failedToSignInMessage)

        val providerToLauncher = model.providers.filter { it != Basic }
            .associateWith { provider ->
                createActivityResultLauncher(provider, authCallback)
            }

        Scaffold(
            topBar = {
                TopBar(title = "Register") {
                    callbackCollection.prev()
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Image(
                        painter = painterResource(R.drawable.signup_divider),
                        contentDescription = "",
                        modifier = Modifier
                            .width(320.dp)
                            .height(21.dp),
                        contentScale = ContentScale.FillWidth
                    )

                    providerToLauncher.forEach { (provider, launcher) ->
                        SignUpComponent.of(provider)({
                            launcher.launch(Unit)
                        })
                    }
                }
            },
            backgroundColor = AppTheme.colors.background
        ) {
            SignUpBody(model)
        }
    }

    private fun createAuthCallback(
        context: Context,
        auth: FirebaseAuth,
        callbackCollection: CallbackCollection,
        @StringRes failedToSignInMessage: Int,
    ) = AuthCallback(
        onSuccess = {
            ViewUtil.showToastMessage(context, "Hello, ${auth.currentUser?.displayName}!")
            callbackCollection.next()
        },
        onFailure = {
            ViewUtil.showToastMessage(context, context.getString(failedToSignInMessage))
        }
    )

    @Composable
    private fun createActivityResultLauncher(provider: SignInProvider, authCallback: AuthCallback) =
        rememberLauncherForActivityResult(
            AuthContractFactory.createAuthContract(
                provider,
                authCallback
            )
        ) { }

    @Composable
    private fun SignUpBody(model: SignUpModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                model.drawableId?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.shadow(elevation = 10.dp, shape = CircleShape, clip = false)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(
                    text = model.title,
                    style = AppTheme.typography.appTitle,
                    color = AppTheme.colors.textPrimary
                )
            }
            Spacer(modifier = Modifier.height(25.dp))

            model.description?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.textPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            if (model.providers.contains(Basic))
                BasicSignUpComponent { }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpViewPreview() =
    SignUpView().Render(
        SignUpModel(
            "id",
            "Register",
            listOf(SignInProvider.Basic, SignInProvider.Google),
            "Thanks for joining the study! Now please create an account to keep track of your data and keep it safe.",
        ),
        CallbackCollection(),
        null
    )
