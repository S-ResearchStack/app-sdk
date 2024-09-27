package researchstack.presentation.screen.insight

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R
import researchstack.domain.model.Study
import researchstack.presentation.LocalNavController
import researchstack.presentation.initiate.route.MainPage
import researchstack.presentation.initiate.route.Route
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.viewmodel.study.StudyListViewModel

@Composable
fun StudyStatusScreen(
    status: Int,
    studyListViewModel: StudyListViewModel = hiltViewModel(),
) {
    LaunchedEffect(null) {
        studyListViewModel.getMyStudy()
    }
    val navController = LocalNavController.current
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            modifier = Modifier
                .height(74.dp)
                .padding(start = 8.dp),

            backgroundColor = AppTheme.colors.background,
            elevation = 0.dp,
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "AccountCircle",
                    tint = AppTheme.colors.primaryVariant
                )
            }
            val text = if (status == 0) {
                stringResource(id = R.string.registered_study_title)
            } else stringResource(id = R.string.completed_study_title)
            Text(
                text,
                style = AppTheme.typography.title1,
                color = AppTheme.colors.onSurface
            )
        }
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (status == 0) {
                val myActiveStudies = studyListViewModel.myActiveStudies.collectAsState().value
                if (myActiveStudies.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.no_registered_message),
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "",
                            modifier = Modifier.size(150.dp)
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(150.dp)
                                .height(43.dp)
                                .background(
                                    color = Color(0xFF0072DE),
                                    shape = RoundedCornerShape(size = 48.dp)
                                )
                                .clickable {
                                    navController.navigate("${Route.Main.name}/${MainPage.Study.ordinal}")
                                }
                        ) {
                            Text(
                                text = stringResource(id = R.string.join_a_study),
                                color = AppTheme.colors.onPrimary
                            )
                        }
                    }
                } else {
                    MyStudyList(myActiveStudies)
                }
            } else {
                val myCompletedStudies =
                    studyListViewModel.myCompletedStudies.collectAsState().value
                if (myCompletedStudies.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.no_completed_message),
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "",
                            modifier = Modifier.size(150.dp)
                        )
                    }
                } else {
                    MyStudyList(myCompletedStudies)
                }
            }
        }
    }
}

@Composable
private fun MyStudyList(studies: List<Study>) {
    studies.forEach {
        StudyCard(study = it)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
