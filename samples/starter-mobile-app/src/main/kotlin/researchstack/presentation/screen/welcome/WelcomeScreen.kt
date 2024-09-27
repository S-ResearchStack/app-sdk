package researchstack.presentation.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R
import researchstack.auth.domain.model.BasicAuthentication
import researchstack.auth.domain.model.OidcAuthentication
import researchstack.auth.domain.repository.AuthRepository
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.LoadingIndicator
import researchstack.presentation.initiate.route.MainPage
import researchstack.presentation.initiate.route.Route.Main
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.descriptionColor
import researchstack.presentation.util.showMessage
import researchstack.presentation.viewmodel.welcome.WelcomeViewModel
import researchstack.presentation.viewmodel.welcome.WelcomeViewModel.Fail
import researchstack.presentation.viewmodel.welcome.WelcomeViewModel.Registering
import researchstack.presentation.viewmodel.welcome.WelcomeViewModel.Success

@Composable
fun WelcomeScreen(welcomeViewModel: WelcomeViewModel = hiltViewModel()) {
    val registerStatus = welcomeViewModel.registerState.collectAsState().value
    val context = LocalContext.current

    WelcomeView(welcomeViewModel)

    when (registerStatus) {
        Registering -> LoadingIndicator()

        Success -> LocalNavController.current.navigate("${Main.name}/${MainPage.Study.ordinal}") {
            popUpTo(0)
        }

        is Fail -> {
            context.showMessage(registerStatus.message)
        }

        else -> {}
    }
}

@Composable
private fun WelcomeView(
    welcomeViewModel: WelcomeViewModel,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    painter = painterResource(id = R.drawable.health_research_main_logo),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillHeight,
                )
                Spacer(modifier = Modifier.height(17.dp))

                Text(
                    modifier = Modifier
                        .width(305.dp)
                        .wrapContentHeight(),
                    text = stringResource(id = R.string.app_name),
                    style = AppTheme.typography.headline2,
                    color = AppTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .width(240.dp)
                        .wrapContentHeight(),
                    text = stringResource(id = R.string.app_message),
                    style = AppTheme.typography.body1,
                    color = descriptionColor,
                    textAlign = TextAlign.Center,
                )
            }

            when (welcomeViewModel.authType) {
                AuthRepository.AuthType.ID_PASSWORD ->
                    IdPasswordSignInView(welcomeViewModel)
                AuthRepository.AuthType.OIDC ->
                    OidcSignInView(welcomeViewModel)
            }
        }
    }
}

@Composable
fun IdPasswordSignInView(welcomeViewModel: WelcomeViewModel) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextFieldWithTitle(
            title = stringResource(id = R.string.email),
            keyboardType = KeyboardType.Email,
        ) { input ->
            emailInput = input
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldWithTitle(
            title = stringResource(id = R.string.password),
            keyboardType = KeyboardType.Password,
        ) { input ->
            passwordInput = input
        }
    }

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SignInButton {
            welcomeViewModel.registerUser(BasicAuthentication(emailInput, passwordInput))
        }
    }
}

@Composable
private fun OidcSignInView(
    welcomeViewModel: WelcomeViewModel,
) {
    SignInButton(70.dp) {
        welcomeViewModel.registerUser(OidcAuthentication)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TextFieldWithTitle(
    title: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    var userInput by remember { mutableStateOf("") }
    var inputVisible by rememberSaveable {
        mutableStateOf(keyboardType != KeyboardType.Password)
    }

    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = AppTheme.typography.body3,
            color = descriptionColor,
            textAlign = TextAlign.Left,
        )

        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = userInput,
            onValueChange = {
                userInput = it
                onValueChange(userInput)
            },
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.primary,
                    shape = RoundedCornerShape(size = 4.dp)
                ),
            singleLine = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                textColor = AppTheme.colors.primary,
                backgroundColor = Color(@Suppress("MagicNumber") 0xFFF6F6F6),
            ),
            textStyle = AppTheme.typography.body1,
            visualTransformation = if (inputVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    val image = if (inputVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (inputVisible) "Hide password" else "Show password"

                    IconButton(onClick = { inputVisible = !inputVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            }
        )
    }
}

@Composable
private fun SignInButton(
    bottomPadding: Dp = 24.dp,
    onButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(bottom = bottomPadding)
            .fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppTextButton(stringResource(R.string.login)) {
                onButtonClick()
            }
        }
    }
}
