package healthstack.kit.task.survey.view

import android.content.Context
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.branchlogicengine.evalExpression

import healthstack.kit.R.string
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.datastore.PreferenceDataStore
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.model.SurveyModel
import healthstack.kit.task.survey.question.QuestionSubStep
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.task.survey.question.component.ChoiceQuestionComponent
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomBar
import healthstack.kit.ui.BottomRoundButton
import healthstack.kit.ui.TopBar
import healthstack.kit.ui.util.ViewUtil
import kotlinx.coroutines.launch
import org.json.JSONObject

open class SurveyView(
    private val pageable: Boolean = true,
    private val isEligibility: Boolean = false,
    private val isSurveyWithSection: Boolean = false,
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
        if (isSurveyWithSection)
            SectionPageSurveyLayout(model, callbackCollection, subStepHolder)
        else if (pageable)
            MultiPageSurveyLayout(model, callbackCollection, subStepHolder, isEligibility)
        else
            SinglePageSurveyLayout(model, callbackCollection, subStepHolder, isEligibility)
    }
}

@Composable
fun MultiPageSurveyLayout(
    model: SurveyModel,
    callbackCollection: CallbackCollection,
    subStepHolder: SubStepHolder,
    isEligibility: Boolean,
) {
    var index by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferenceDataStore = PreferenceDataStore(context)
    val subStep = subStepHolder.subSteps.first()

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
                    if (subStep[index].model.getResponse() == null) {
                        ViewUtil.showToastMessage(context, "Please input answer")
                        return@BottomBar
                    }

                    if (index == subStep.size - 1) {
                        callbackCollection.setEligibility(subStepHolder.isSufficient())
                        if (isEligibility)
                            scope.launch {
                                val profile: Map<String, Any?> = subStep.associate {
                                    it.model.id to it.model.getResponse()
                                }
                                preferenceDataStore.setProfile(JSONObject(profile).toString())
                            }
                        callbackCollection.next()
                        return@BottomBar
                    }
                    index += 1
                },
                leftButtonEnabled = index != 0
            )
        },
        backgroundColor = AppTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            SurveyProgressView(
                "${index + 1} out of ${subStep.size}",
                (index + 1) / subStep.size.toFloat()
            )
            Spacer(modifier = Modifier.height(30.dp))
            subStep[index].Render(callbackCollection)
        }
    }
}

@Composable
private fun getNextButtonMessage(index: Int, subStepHolder: SubStepHolder?, context: Context) =
    if (index == subStepHolder!!.subSteps[0].size - 1) context.getString(string.complete)
    else context.getString(string.next)

@Composable
fun SurveyProgressView(progressText: String, progress: Float) {
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp)
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(50.dp)),
            color = AppTheme.colors.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            progressText,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.onSurface
        )
    }
}

@Composable
fun SinglePageSurveyLayout(
    model: SurveyModel,
    callbackCollection: CallbackCollection,
    subStepHolder: SubStepHolder,
    isEligibility: Boolean,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferenceDataStore = PreferenceDataStore(context)
    val subStep = subStepHolder.subSteps.first()

    Scaffold(
        topBar = {
            TopBar(title = model.title) {
                callbackCollection.prev()
            }
        },
        backgroundColor = AppTheme.colors.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            subStep.forEachIndexed { _, questionSubStep ->
                Row(
                    Modifier.padding(horizontal = 24.dp)
                ) {
                    Column {
                        questionSubStep.Render(callbackCollection)
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }
            }
            Row {
                BottomRoundButton(text = LocalContext.current.getString(string.submit)) {
                    callbackCollection.setEligibility(subStepHolder.isSufficient())
                    if (isEligibility)
                        scope.launch {
                            val profile: Map<String, Any?> = subStep.associate {
                                it.model.id to it.model.getResponse()
                            }
                            preferenceDataStore.setProfile(JSONObject(profile).toString())
                        }
                    callbackCollection.next()
                }
            }
        }
    }
}

@Composable
fun SectionPageSurveyLayout(
    model: SurveyModel,
    callbackCollection: CallbackCollection,
    subStepHolder: SubStepHolder,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var index by remember { mutableStateOf(0) }
    val navigateStack = remember { ArrayDeque<Int>() }

    Scaffold(
        topBar = {
            TopBar(title = model.title) {
                callbackCollection.prev()
            }
        },
        bottomBar = {
            BottomBar(
                leftButtonText = context.getString(string.previous),
                rightButtonText = if (index == subStepHolder.subSteps.size - 1) context.getString(string.complete)
                else context.getString(string.next),
                onClickLeftButton = { index = navigateStack.removeLast() },
                onClickRightButton = {
                    if (subStepHolder.subSteps[index].any { it.model.getResponse() == null }
                    ) {
                        ViewUtil.showToastMessage(context, "Please input answer")
                        return@BottomBar
                    }

                    if (index == subStepHolder.subSteps.size - 1) {
                        callbackCollection.next()
                        return@BottomBar
                    }

                    val contextValueMap = subStepHolder.subSteps.flatMap { section ->
                        section.map {
                            it.model.id.replace("Question", "val") to it.getResult().toString()
                        }
                    }.toMap()

                    val selectedSkipLogic = subStepHolder.subSteps[index].flatMap {
                        it.model.skipLogics
                    }.findLast { evalExpression(it.condition, contextValueMap) }

                    navigateStack.addLast(index)
                    if (selectedSkipLogic == null) {
                        index += 1
                    } else {
                        val destination = subStepHolder.subSteps.flatMapIndexed { index, section ->
                            section.filter {
                                it.model.id.replace("Question", "")
                                    .replace("Section", "").toInt() >= selectedSkipLogic.goToItemSequence
                            }.map { index }
                        }
                        index = destination.first()
                    }
                },
                leftButtonEnabled = index != 0
            )
        },
        backgroundColor = AppTheme.colors.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            SurveyProgressView(
                "${index + 1} out of ${subStepHolder.subSteps.size}",
                (index + 1) / subStepHolder.subSteps.size.toFloat()
            )
            Spacer(modifier = Modifier.height(28.dp))
            subStepHolder.subSteps[index].forEachIndexed { _, questionSubStep ->
                questionSubStep.Render(callbackCollection)
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun EligibilityCheckerViewPreview() {
    val view = SurveyView(true)
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
        listOf(questionnaireSubSteps),
    )

    view.Render(
        model,
        CallbackCollection(),
        subStepHolder
    )
}
