package researchstack.presentation.screen.task

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R
import researchstack.domain.model.task.Section
import researchstack.domain.model.task.SurveyTask
import researchstack.domain.model.task.question.ChoiceQuestion
import researchstack.domain.model.task.question.DateTimeQuestion
import researchstack.domain.model.task.question.RankQuestion
import researchstack.domain.model.task.question.ScaleQuestion
import researchstack.domain.model.task.question.TextQuestion
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.LoadingIndicator
import researchstack.presentation.component.TopBar
import researchstack.presentation.initiate.route.MainPage
import researchstack.presentation.initiate.route.Route.Main
import researchstack.presentation.screen.task.question.ChoiceQuestionCard
import researchstack.presentation.screen.task.question.DateTimeQuestionCard
import researchstack.presentation.screen.task.question.RankingQuestionCard
import researchstack.presentation.screen.task.question.ScaleQuestionCard
import researchstack.presentation.screen.task.question.TextQuestionCard
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.util.showMessage
import researchstack.presentation.viewmodel.task.TaskViewModel
import researchstack.presentation.viewmodel.task.TaskViewModel.TaskState.Complete
import researchstack.presentation.viewmodel.task.TaskViewModel.TaskState.Saving
import java.time.LocalDateTime

@Composable
fun SurveyTaskScreen(
    task: SurveyTask,
    taskViewModel: TaskViewModel = hiltViewModel(),
) {
    val surveyResult = mutableMapOf<String, String>()
    val taskState = taskViewModel.taskState.collectAsState().value
    val context = LocalContext.current

    // FIXME passing surveyResult is not good way
    SurveyTaskView(task.title, task.sections, surveyResult) {
        Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
            AppTextButton(
                text = stringResource(R.string.submit),
                enabled = taskState == TaskViewModel.TaskState.Init,
            ) {
                if (didAnswerAllRequiredQuestions(surveyResult, task.sections)) {
                    context.showMessage(context.getString(R.string.task_done_message))
                    taskViewModel.saveTaskResult(
                        SurveyResult(
                            task.id ?: 0,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            surveyResult.map { QuestionResult(it.key, it.value) }
                        )
                    )
                } else {
                    context.showMessage(context.getString(R.string.not_completed_task_message))
                }
            }
        }
    }

    if (taskState == Saving) {
        LoadingIndicator()
    } else if (taskState == Complete) {
        LocalNavController.current.navigate("${Main.name}/${MainPage.Task.ordinal}") {
            popUpTo(0)
        }
    }
}

@Composable
fun SurveyTaskView(
    title: String,
    sections: List<Section>,
    surveyResult: MutableMap<String, String>,
    bottomBar: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(1f),
        topBar = {
            TopBar(title = title)
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)

        ) {
            Spacer(modifier = Modifier.height(8.dp))
            sections.forEach { (questions) ->
                questions.forEach { question ->
                    Column(modifier = Modifier.padding(10.dp)) {
                        QuestionCommon(question)
                        Spacer(modifier = Modifier.height(16.dp))
                        when (question) {
                            is ChoiceQuestion -> ChoiceQuestionCard(question) {
                                surveyResult[question.id] = it
                            }

                            is TextQuestion -> TextQuestionCard(question) {
                                surveyResult[question.id] = it
                            }

                            is RankQuestion -> RankingQuestionCard(question) {
                                surveyResult[question.id] = it
                            }

                            is ScaleQuestion -> ScaleQuestionCard(question) {
                                surveyResult[question.id] = it
                            }

                            is DateTimeQuestion -> DateTimeQuestionCard(question) {
                                surveyResult[question.id] = it
                            }

                            else -> {
                                Log.e("SurveyTaskView", "question $question")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun QuestionCommon(question: Question) {
    Text(
        text = question.title,
        style = AppTheme.typography.title2,
        color = AppTheme.colors.onSurface,
    )

    if (question.explanation.isNotBlank()) {
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Text(
            text = question.explanation,
            modifier = Modifier
                .fillMaxWidth(),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.onBackground.copy(@Suppress("MagicNumber") 0.6F)
        )
    }
}

fun didAnswerAllRequiredQuestions(
    surveyResult: MutableMap<String, String>,
    sections: List<Section>,
): Boolean = surveyResult.keys.containsAll(
    sections.flatMap {
        it.questions
            .filter { q -> q.isRequired }
            .map { q -> q.id }
    }
)
