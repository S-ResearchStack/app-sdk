package researchstack.presentation.screen.welcome

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.domain.model.UserProfileModel
import researchstack.presentation.LocalNavController
import researchstack.presentation.initiate.route.MainPage
import researchstack.presentation.initiate.route.Route
import researchstack.presentation.util.MyTextField
import researchstack.presentation.util.showErrorToast
import researchstack.presentation.viewmodel.welcome.UserRegisterViewModel
import java.time.LocalDate

private const val TAG = "RegisterScreen"

@Composable
fun RegisterScreen(userRegisterViewModel: UserRegisterViewModel = hiltViewModel()) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var email1 by remember { mutableStateOf("") }
    var email2 by remember { mutableStateOf("") }
    var phoneNumber1 by remember { mutableStateOf("") }
    var phoneNumber2 by remember { mutableStateOf("") }
    var phoneNumber3 by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var okButtonEnabled by remember { mutableStateOf(false) }
    var isOkButtonClicked by remember { mutableStateOf(false) }

    val registerResult by userRegisterViewModel.registerResult.observeAsState(null)

    fun checkAndEnableOkButton() {
        // TODO: Integrity verification required for each field
        val fields = listOf(
            firstName, lastName, birthday, email1, email2,
            phoneNumber1, phoneNumber2, phoneNumber3, address
        )

        if (fields.all { it.isNotEmpty() })
            okButtonEnabled = true
    }

    when (getRegisterStatus(context, isOkButtonClicked, registerResult)) {
        RegisterStatus.Initial -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp)
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding(),
                horizontalAlignment = Alignment.Start
            ) {
                TextFieldWithLabel("Name") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MyTextField(
                            modifier = Modifier.width(150.dp),
                            value = lastName,
                            onValueChange = {
                                lastName = it
                                checkAndEnableOkButton()
                            },
                            hintText = "Last name"
                        )
                        MyTextField(
                            modifier = Modifier.width(150.dp),
                            value = firstName,
                            onValueChange = {
                                firstName = it
                                checkAndEnableOkButton()
                            },
                            hintText = "First name"
                        )
                    }
                }

                TextFieldWithLabel("Birth date") {
                    MyTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = birthday,
                        onValueChange = {
                            birthday = it
                            checkAndEnableOkButton()
                        },
                        hintText = "19931215",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                TextFieldWithLabel("Phone number") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MyTextField(
                            modifier = Modifier.width(80.dp),
                            value = phoneNumber1,
                            onValueChange = {
                                phoneNumber1 = it
                                checkAndEnableOkButton()
                            },
                            hintText = "010",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        MyTextField(
                            modifier = Modifier.width(100.dp),
                            value = phoneNumber2,
                            onValueChange = {
                                phoneNumber2 = it
                                checkAndEnableOkButton()
                            },
                            hintText = "1234",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        MyTextField(
                            modifier = Modifier.width(100.dp),
                            value = phoneNumber3,
                            onValueChange = {
                                phoneNumber3 = it
                                checkAndEnableOkButton()
                            },
                            hintText = "5678",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }

                TextFieldWithLabel("Email") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyTextField(
                            modifier = Modifier.width(140.dp),
                            value = email1,
                            onValueChange = {
                                email1 = it
                                checkAndEnableOkButton()
                            },
                            hintText = "email",
                        )
                        Text("@")
                        MyTextField(
                            modifier = Modifier.width(140.dp),
                            value = email2,
                            onValueChange = {
                                email2 = it
                                checkAndEnableOkButton()
                            },
                            hintText = "naver.com",
                        )
                    }
                }

                TextFieldWithLabel("Address") {
                    MyTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = address,
                        onValueChange = {
                            address = it
                            checkAndEnableOkButton()
                        },
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp)
                        .height(60.dp),
                    onClick = {
                        isOkButtonClicked = true
                        userRegisterViewModel.registerUser(
                            UserProfileModel(
                                firstName = firstName,
                                lastName = lastName,
                                birthday = birthday.birthDayStringToLocalDate(),
                                phoneNumber = phoneNumber1 + phoneNumber2 + phoneNumber3,
                                email = "$email1@$email2",
                                address = address
                            )
                        )
                    },
                    enabled = okButtonEnabled,
                    colors = ButtonDefaults.buttonColors(disabledBackgroundColor = Color.LightGray)
                ) {
                    Text("확인")
                }
            }
        }

        RegisterStatus.Registering -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        RegisterStatus.Succeed -> {
            LocalNavController.current.navigate("${Route.Main.name}/${MainPage.Task.ordinal}") { popUpTo(0) }
        }

        RegisterStatus.Failed -> {
            isOkButtonClicked = false
            userRegisterViewModel.resetResult()
        }
    }
}

private fun getRegisterStatus(
    context: Context,
    isOkButtonClicked: Boolean,
    registerResult: Result<Unit>?
): RegisterStatus {
    return if (!isOkButtonClicked) RegisterStatus.Initial else {
        val isRegisterSucceed = registerResult?.onFailure {
            showErrorToast(context, TAG, it.message ?: "", it)
        }?.isSuccess
        when (isRegisterSucceed) {
            true -> RegisterStatus.Succeed
            false -> RegisterStatus.Failed
            null -> RegisterStatus.Registering
        }
    }
}

enum class RegisterStatus {
    Initial,
    Registering,
    Succeed,
    Failed
}

@Composable
fun TextFieldWithLabel(fieldName: String, textField: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(fieldName)
        Spacer(modifier = Modifier.height(5.dp))
        textField.invoke()
    }
    Spacer(modifier = Modifier.height(30.dp))
}

private fun String.birthDayStringToLocalDate(): LocalDate {
    val year = this.slice(0..3).toInt()
    val month = this.slice(4..5).toInt()
    val day = this.slice(6..7).toInt()
    return LocalDate.of(year, month, day)
}
