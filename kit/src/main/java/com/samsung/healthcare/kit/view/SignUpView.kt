package com.samsung.healthcare.kit.view

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.auth.SignInProvider
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.SignUpModel
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.auth.BasicSignUpComponent
import com.samsung.healthcare.kit.view.auth.SignUpComponent
import com.samsung.healthcare.kit.view.common.TopBar

class SignUpView : View<SignUpModel>() {
    @Composable
    override fun Render(
        model: SignUpModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?
    ) {

        Scaffold(
            topBar = {
                TopBar(title = "Register") {
                    callbackCollection.prev()
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 20.dp)
                ) {
                    model.providers.filter { it != SignInProvider.Basic }
                        .forEach {
                            SignUpComponent.of(it)(callbackCollection)
                        }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
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
                            contentScale = ContentScale.Fit
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
                        color = AppTheme.colors.textPrimary
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
                if (model.providers.contains(SignInProvider.Basic)) {
                    BasicSignUpComponent(callbackCollection)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpViewPreview() =
    SignUpView().Render(
        SignUpModel(
            "id",
            "SleepCare",
            listOf(SignInProvider.Google),
            "Thanks for joining the study! Now please create an account to keep track of your data and keep it safe.",
        ),
        CallbackCollection(),
        null
    )
