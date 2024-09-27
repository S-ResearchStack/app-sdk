package researchstack.presentation.screen.study

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.LoadingIndicator
import researchstack.presentation.initiate.route.Route
import researchstack.presentation.screen.task.SurveyTaskView
import researchstack.presentation.screen.task.didAnswerAllRequiredQuestions
import researchstack.presentation.viewmodel.study.EligibilityViewModel
import java.time.LocalDateTime

@Composable
fun StudyEligibilityScreen(
    studyId: String,
    eligibilityViewModel: EligibilityViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    LaunchedEffect(null) {
        eligibilityViewModel.getParticipationRequirement()
    }

    val participationRequirement =
        eligibilityViewModel.participationRequirement.collectAsState().value
    val dateTime = LocalDateTime.now()
    if (participationRequirement != null &&
        isParticipationRequirementCorrect(participationRequirement, studyId)
    ) {
        if (participationRequirement.eligibilityTest.sections.isEmpty()) {
            setEligibilityTestResult(
                eligibilityViewModel,
                participationRequirement.eligibilityTest.studyId,
                dateTime,
                listOf()
            )

            navController.navigate(Route.InformedConsent.name)
        }

        val surveyResult = mutableMapOf<String, String>()
        SurveyTaskView(
            stringResource(id = R.string.eligibility_title),
            participationRequirement.eligibilityTest.sections,
            surveyResult
        ) {
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 5.dp)) {
                AppTextButton(text = stringResource(id = R.string.confirm)) {
                    if (didAnswerAllRequiredQuestions(
                            surveyResult,
                            participationRequirement.eligibilityTest.sections
                        )
                    ) {
                        if (eligibilityViewModel.checkEligibility(surveyResult)) {
                            setEligibilityTestResult(
                                eligibilityViewModel,
                                participationRequirement.eligibilityTest.studyId,
                                dateTime,
                                surveyResult.map { (key, value) ->
                                    QuestionResult(key, value)
                                }
                            )

                            navController.navigate(Route.InformedConsent.name)
                        } else {
                            navController.navigate(Route.EligibilityFailed.name)
                        }
                    } else {
                        Toast.makeText(context, "필수 질문에 .....", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    } else {
        LoadingIndicator()
    }
}

private fun isParticipationRequirementCorrect(
    participationRequirement: ParticipationRequirement,
    studyId: String,
) =
    participationRequirement.eligibilityTest.studyId == studyId

private fun setEligibilityTestResult(
    eligibilityViewModel: EligibilityViewModel,
    studyId: String,
    startedAt: LocalDateTime,
    surveyResult: List<QuestionResult>,
) {
    val finishedAt = LocalDateTime.now()

    eligibilityViewModel.setEligibilityTestResult(
        EligibilityTestResult(
            studyId,
            SurveyResult(
                // TODO what is id?
                1,
                startedAt,
                finishedAt,
                surveyResult,
            )
        )
    )
}
