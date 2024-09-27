package researchstack.presentation.screen.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R.string
import researchstack.domain.model.Study
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.TopBar
import researchstack.presentation.initiate.route.Route.StudyCode
import researchstack.presentation.initiate.route.Route.StudyInformation
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.descriptionColor
import researchstack.presentation.viewmodel.study.StudyListViewModel

@Composable
fun StudyListScreen(
    studyListViewModel: StudyListViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current

    LaunchedEffect(null) {
        studyListViewModel.lookupStudy()
    }
    val studies = studyListViewModel.studies.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(1f),
        topBar = {
            TopBar(title = LocalContext.current.getString(string.study_list_title))
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, top = 8.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppTextButton(
                stringResource(id = string.study_code_button),
                AppTheme.colors.primary,
                AppTheme.colors.background
            ) {
                navController.navigate(StudyCode.name)
            }
            if (studies.isEmpty()) {
                NoStudyMessage()
            } else {
                Spacer(modifier = Modifier.height(24.dp))
                studies.forEach {
                    StudyCard(study = it)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun NoStudyMessage() {
    Spacer(Modifier.height(230.dp))
    Text(
        text = stringResource(id = string.no_study_message),
        style = AppTheme.typography.body1,
        color = descriptionColor,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun StudyCard(
    study: Study,
    viewModel: StudyListViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val shape = RoundedCornerShape(4.dp)

    Card(
        shape = shape,
        backgroundColor = AppTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .height(312.dp)
            .shadow(elevation = 2.dp, shape = shape, clip = false)
            .clickable {
                viewModel.setStudy(study)
                navController.navigate(StudyInformation.name)
            },
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Image(
                painter = study.geStudyPainter(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(139.dp),
                contentScale = Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = study.name,
                    style = AppTheme.typography.title1,
                    color = AppTheme.colors.onSurface,
                )
                Spacer(modifier = Modifier.height(12.dp))

                StudyTimeInformation(study, descriptionColor)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = study.description,
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.onSurface,
                    maxLines = 1,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp, start = 16.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = study.organization,
                    style = AppTheme.typography.overline1.copy(
                        color = descriptionColor,
                        letterSpacing = 0.3.sp,
                    )
                )
            }
        }
    }
}
