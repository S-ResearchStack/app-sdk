package researchstack.presentation.screen.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R.drawable
import researchstack.R.string
import researchstack.domain.exception.EmptyStudyCodeException
import researchstack.domain.exception.NotFoundStudyException
import researchstack.domain.exception.UnableToResolveHostException
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.LoadingIndicator
import researchstack.presentation.initiate.route.Route.StudyInformation
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.descriptionColor
import researchstack.presentation.viewmodel.study.StudyCodeViewModel
import researchstack.presentation.viewmodel.study.StudyCodeViewModel.Fail
import researchstack.presentation.viewmodel.study.StudyCodeViewModel.Init
import researchstack.presentation.viewmodel.study.StudyCodeViewModel.Loading
import researchstack.presentation.viewmodel.study.StudyCodeViewModel.Success

@Composable
fun StudyCodeInputScreen() {
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = null
                    )
                }
                Text(
                    text = "Enter study code",
                    style = AppTheme.typography.title1,
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .wrapContentHeight(),
                text = stringResource(id = string.input_study_code_message),
                style = AppTheme.typography.body2,
                color = descriptionColor,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(150.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = string.study_code),
                    style = AppTheme.typography.body3,
                    color = descriptionColor,
                    textAlign = TextAlign.Left,
                )
                Image(
                    modifier = Modifier.size(6.dp),
                    imageVector = ImageVector.vectorResource(drawable.aterisk),
                    contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            StudyCodeInput()
        }
    }
}

@Composable
private fun StudyCodeInput(
    studyCodeViewModel: StudyCodeViewModel = hiltViewModel(),
) {
    val state = studyCodeViewModel.state.collectAsState().value
    var errorMessage by remember {
        mutableStateOf("")
    }

    StudyCodeTextField(errorMessage) {
        if (it.isEmpty()) studyCodeViewModel.handleEmptyStudyCode()
        else studyCodeViewModel.getClosedStudy(it)
    }

    when (state) {
        is Fail -> {
            errorMessage =
                when (state.error) {
                    is NotFoundStudyException -> stringResource(id = string.study_code_not_found)
                    is EmptyStudyCodeException -> stringResource(id = string.study_code_empty)
                    is UnableToResolveHostException -> stringResource(id = string.study_code_check_network)
                    else -> stringResource(id = string.study_code_unexpected_error)
                }
            studyCodeViewModel.setStateToInit()
        }

        Loading -> LoadingIndicator()
        Init -> {
            // Nothing
        }

        is Success -> {
            studyCodeViewModel.setStateToInit()
            studyCodeViewModel.setStudy(state.study)
            LocalNavController.current.navigate(StudyInformation.name)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun StudyCodeTextField(
    errorMessage: String,
    onClick: (String) -> Unit,
) {
    var studyCode by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    TextField(
        value = studyCode,
        onValueChange = { studyCode = it },
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = AppTheme.colors.primary,
                shape = RoundedCornerShape(size = 4.dp)
            )
            .focusRequester(focusRequester),
        singleLine = true,
        colors = textFieldColors(
            textColor = AppTheme.colors.primary,
            backgroundColor = Color(@Suppress("MagicNumber") 0xFFF6F6F6),
        ),
        textStyle = AppTheme.typography.body1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )

    Text(
        text = errorMessage,
        fontSize = 12.sp,
        color = Color.Red,
        modifier = Modifier.fillMaxWidth()
    )

    Row(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .padding(horizontal = 40.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
    ) {
        AppTextButton(stringResource(string.join_study_message)) {
            onClick(studyCode)
        }
    }
}
