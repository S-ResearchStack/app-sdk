package researchstack.presentation.screen.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R.drawable
import researchstack.R.string
import researchstack.domain.model.Study
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.TopBar
import researchstack.presentation.initiate.route.Route.StudyEligibility
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.descriptionColor
import researchstack.presentation.viewmodel.study.StudyViewModel

@Composable
fun StudyInformationScreen(
    studyViewModel: StudyViewModel = hiltViewModel(),
) {
    val study = studyViewModel.study.collectAsState().value

    if (study == null) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        IntroScreen(study)
    }
}

@Composable
private fun IntroScreen(study: Study) {
    val scrollState = rememberScrollState()
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            TopBar(title = stringResource(id = string.study_informaion))
        },
        bottomBar = {
            Row(modifier = Modifier.padding(20.dp)) {
                AppTextButton(text = stringResource(id = string.join_study_message)) {
                    navController.navigate("${StudyEligibility.name}/${study.id}")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .verticalScroll(scrollState),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                painter = study.geStudyPainter(),
                contentDescription = "study image",
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    text = study.name,
                    style = AppTheme.typography.title1,
                    color = AppTheme.colors.onSurface,
                )

                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = study.organization,
                    style = AppTheme.typography.body3,
                    color = descriptionColor,
                )

                StudyTimeInformation(study)

                IntroSections(study)
            }
        }
    }
}

@Composable
fun StudyTimeInformation(
    study: Study,
    textColor: Color = descriptionColor,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(drawable.clock),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
                .padding(1.dp),
            tint = AppTheme.colors.primary,
        )
        Text(
            text = study.duration,
            style = AppTheme.typography.body3,
            color = textColor,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = ImageVector.vectorResource(drawable.calendar),
            contentDescription = "calendar_icon",
            modifier = Modifier
                .size(24.dp)
                .padding(1.dp),
            tint = AppTheme.colors.primary,
        )
        Text(
            text = study.period,
            style = AppTheme.typography.body3,
            color = textColor,
        )
    }
}

@Composable
internal fun Study.geStudyPainter() = painterResource(drawable.study_image)

@Composable
private fun IntroSections(study: Study): Unit =
    Column(
        modifier = Modifier
            .padding(top = 28.dp)
            .wrapContentHeight()
            .background(AppTheme.colors.background)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = string.study_introduction),
            style = AppTheme.typography.title3,
            color = AppTheme.colors.onSurface
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = study.description,
            style = AppTheme.typography.body3,
            color = AppTheme.colors.onSurface
        )
        Spacer(Modifier.size(24.dp))
        Text(
            text = stringResource(id = string.study_requirements),
            style = AppTheme.typography.title3,
            color = AppTheme.colors.onSurface
        )
        Spacer(Modifier.size(8.dp))
        if (study.requirements.isEmpty()) {
            Text(
                text = stringResource(id = string.no_requirement_message),
                style = AppTheme.typography.body3,
                color = descriptionColor,
            )
        } else {
            study.requirements.forEach {
                Text(
                    text = "$it",
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.onSurface
                )
            }
        }
    }
