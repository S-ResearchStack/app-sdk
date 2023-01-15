package healthstack.kit.task.survey.view

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R.string
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.model.SurveyModel
import healthstack.kit.task.survey.question.QuestionSubStep
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.task.survey.question.component.ChoiceQuestionComponent
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomBar
import healthstack.kit.ui.BottomSquareButton
import healthstack.kit.ui.TopBar
import healthstack.kit.ui.util.ViewUtil

open class SurveyView(
    private val pageable: Boolean = true,
) : View<SurveyModel>() {

    @Composable
    override fun Render(
        model: SurveyModel,
        callbackCollection: CallbackCollection,
        subStepHolder: SubStepHolder?,
    ) {
        requireNotNull(subStepHolder)

        BackHandler(true) {
            callbackCollection.prev()
        }

        if (pageable)
            MultiPageSurveyLayout(model, callbackCollection, subStepHolder)
        else
            SinglePageSurveyLayout(model, callbackCollection, subStepHolder)
    }
}

@Composable
fun MultiPageSurveyLayout(
    model: SurveyModel,
    callbackCollection: CallbackCollection,
    subStepHolder: SubStepHolder,
) {
    var index by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopBar(title = model.title) {
                callbackCollection.prev()
            }
        },
        bottomBar = {
            BottomBar(
                leftButtonText = context.getString(string.previous),
                rightButtonText = getNextButtonMessage(index, subStepHolder, context),
                onClickLeftButton = { index -= 1 },
                onClickRightButton = {
                    if (subStepHolder.subSteps[index].model.getResponse() == null) {
                        ViewUtil.showToastMessage(context, "please input answer")
                        return@BottomBar
                    }

                    if (index == subStepHolder.size - 1) {
                        callbackCollection.setEligibility(subStepHolder.isSufficient())
                        callbackCollection.next()
                        return@BottomBar
                    }
                    index += 1
                },
                leftButtonEnabled = index != 0
            )
        },
        backgroundColor = AppTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            SurveyProgressView(
                "${index + 1}/${subStepHolder.size}",
                (index + 1) / subStepHolder.size.toFloat()
            )
            Spacer(modifier = Modifier.height(64.dp))
            subStepHolder.subSteps[index].Render(callbackCollection)
        }
    }
}

@Composable
private fun getNextButtonMessage(index: Int, subStepHolder: SubStepHolder?, context: Context) =
    if (index == subStepHolder!!.size - 1) context.getString(string.complete) else context.getString(string.next)

@Composable
fun SurveyProgressView(progressText: String, progress: Float) {
    Column {
        Text(progressText)
        Spacer(modifier = Modifier.height(15.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth(1f),
            color = AppTheme.colors.primary
        )
    }
}

@Composable
fun SinglePageSurveyLayout(
    model: SurveyModel,
    callbackCollection: CallbackCollection,
    subStepHolder: SubStepHolder,
) {
    val scrollSate = rememberScrollState()
    Scaffold(
        topBar = {
            TopBar(title = model.title) {
                callbackCollection.prev()
            }
        },
        backgroundColor = AppTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollSate)
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            subStepHolder.subSteps.forEachIndexed { _, questionSubStep ->
                questionSubStep.Render(callbackCollection)
                Spacer(modifier = Modifier.height(48.dp))
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            BottomSquareButton(text = LocalContext.current.getString(string.submit)) {
                callbackCollection.setEligibility(subStepHolder.isSufficient())
                callbackCollection.next()
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun EligibilityCheckerViewPreview() {
    val view = SurveyView(false)
    val model = SurveyModel(
        id = "eligibility",
        title = "Eligibility",
    )

    val questionnaireSubSteps = listOf(
        QuestionSubStep(
            "question-1",
            "Question-Name-1",
            ChoiceQuestionModel(
                "choice-question-model-1",
                "1. Are you between 30 and 50 years old?",
                "",
                candidates = listOf("Yes", "No", "Prefer not to answer"),
                answer = "Yes"
            ),
            ChoiceQuestionComponent(),
        ),
        QuestionSubStep(
            "question-2",
            "Question-Name-2",
            ChoiceQuestionModel(
                "choice-question-model-2",
                "2. Do you have a family history of cardiovascular diseases?",
                "Examples include stroke, heart attack, high blood pressure, etc.",
                candidates = listOf("Yes", "No"),
                answer = "Yes"
            ),
            ChoiceQuestionComponent(),
        ),
        QuestionSubStep(
            "question-3",
            "Question-Name-3",
            ChoiceQuestionModel(
                "choice-question-model-3",
                "3. Do you take any cardiovscular disease medications?",
                "Examples inlcude Benazepril, Moexipril, Quinapril, etc.",
                candidates = listOf("Yes", "No"),
                answer = "Yes"
            ),
            ChoiceQuestionComponent(),
        ),
        QuestionSubStep(
            "question-4",
            "Question-Name-4",
            ChoiceQuestionModel(
                "choice-question-model-4",
                "4. Do you currently own a wearable device?",
                "Examples of wearable devices include Samsung Galaxy Watch 4, Fitbit, OuraRing, etc.",
                candidates = listOf("Yes", "No"),
                answer = "Yes"
            ),
            ChoiceQuestionComponent(),
        )
    )

    val subStepHolder = SubStepHolder(
        "eligibility",
        "eligibility-checker",
        questionnaireSubSteps
    )

    view.Render(
        model,
        CallbackCollection(),
        subStepHolder
    )
}
