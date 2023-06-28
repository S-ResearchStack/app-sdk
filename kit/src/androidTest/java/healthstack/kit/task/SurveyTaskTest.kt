package healthstack.kit.task

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import healthstack.kit.R
import healthstack.kit.task.survey.SurveyTask
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel
import healthstack.kit.task.survey.question.model.ImageChoiceQuestionModel
import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel
import healthstack.kit.task.survey.question.model.RankingQuestionModel
import healthstack.kit.task.survey.question.model.TextInputQuestionModel
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SurveyTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private val multiChoiceQuestionModel1 = MultiChoiceQuestionModel(
        id = "q-1",
        query = "choice multiple answer",
        explanation = "this is a sample for MultiChoiceQuestionModel",
        drawableId = null,
        answer = null,
        candidates = listOf("a", "b", "c"),
        tag = "checkbox"
    )

    private val multiChoiceQuestionModel2 = ImageChoiceQuestionModel(
        id = "q-1b",
        query = "choice multiple answer",
        explanation = "this is a sample for MultiChoiceQuestionModel",
        drawableId = null,
        answer = null,
        candidates = listOf(
            "https://picsum.photos/seed/picsum/200",
            "https://picsum.photos/seed/picsum/300",
            "https://picsum.photos/seed/picsum/400",
            "https://picsum.photos/seed/picsum/500"
        ),
        tag = "image"
    )

    private val multiChoiceQuestionModel3 = ImageChoiceQuestionModel(
        id = "q-1c",
        query = "choice multiple answer",
        explanation = "this is a sample for MultiChoiceQuestionModel",
        drawableId = null,
        answer = null,
        candidates = listOf(
            "https://picsum.photos/seed/picsum/200",
            "https://picsum.photos/seed/picsum/300",
            "https://picsum.photos/seed/picsum/400",
            "https://picsum.photos/seed/picsum/500"
        ),
        labels = listOf(
            "random image1",
            "random image2",
            "random image3",
            "random image4"
        ),
        tag = "image"
    )

    private val textInputQuestionModel = TextInputQuestionModel(
        id = "q-2",
        query = "input some text",
        explanation = "sample for text input",
        drawableId = R.drawable.ic_task,
        answer = "answer",
    )

    private val dateTimeQuestionModel1 = DateTimeQuestionModel(
        id = "q-3a",
        query = "select something",
        isTime = true,
        isDate = false,
        isRange = false
    )

    private val dateTimeQuestionModel2 = DateTimeQuestionModel(
        id = "q-3b",
        query = "select something",
        isTime = false,
        isDate = true,
        isRange = false
    )

    private val dateTimeQuestionModel3 = DateTimeQuestionModel(
        id = "q-3c",
        query = "select something",
        isTime = true,
        isDate = true,
        isRange = false
    )

    private val dateTimeQuestionModel4 = DateTimeQuestionModel(
        id = "q-3d",
        query = "select something",
        isTime = true,
        isDate = false,
        isRange = true
    )

    private val dateTimeQuestionModel5 = DateTimeQuestionModel(
        id = "q-3e",
        query = "select something",
        isTime = false,
        isDate = true,
        isRange = true
    )

    private val dateTimeQuestionModel6 = DateTimeQuestionModel(
        id = "q-3f",
        query = "select something",
        isTime = true,
        isDate = true,
        isRange = true
    )

    private val rankingQuestionModel = RankingQuestionModel(
        id = "id",
        query = "ranking",
        candidates = listOf("차은우", "정해인", "정국", "에릭")
    )

    private val surveyTask = SurveyTask.Builder(
        id = "task-1",
        revisionId = 1,
        taskId = "survey-sample",
        name = "sample survey",
        description = "test",
        callback = {},
    ).apply {
        addQuestion(multiChoiceQuestionModel1)
        addQuestion(multiChoiceQuestionModel2)
        addQuestion(multiChoiceQuestionModel3)
        addQuestion(textInputQuestionModel)
        addQuestion(dateTimeQuestionModel1)
        addQuestion(dateTimeQuestionModel2)
        addQuestion(dateTimeQuestionModel3)
        addQuestion(dateTimeQuestionModel4)
        addQuestion(dateTimeQuestionModel5)
        addQuestion(dateTimeQuestionModel6)
        addQuestion(rankingQuestionModel)
    }.build()

    @Test
    fun testSurveyTaskCardView() {
        surveyTask.isCompleted = false
        rule.setContent {
            AppTheme(mainLightColors()) {
                surveyTask.CardView {
                }
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
    }

    @Test
    fun testCompletedSurveyTaskCardView() {
        surveyTask.isCompleted = true
        rule.setContent {
            AppTheme(mainLightColors()) {
                surveyTask.CardView {
                }
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertDoesNotExist()
    }

    @Test
    fun testSurveyTask() {
        surveyTask.isCompleted = false
        var completed = false
        surveyTask.callback = { completed = true }

        val step = surveyTask.step
        assertNotNull(step.id)
        assertNotNull(step.name)

        val state = step.getState()
        assertNotNull(state.id)
        assertNotNull(state.title)

        rule.setContent {
            AppTheme(mainLightColors()) {
                surveyTask.Render()
            }
        }

        rule.onNodeWithTag(multiChoiceQuestionModel1.candidates.random())
            .performClick()

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onNodeWithTag(multiChoiceQuestionModel2.candidates.random())
            .performClick()

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onNodeWithTag(multiChoiceQuestionModel3.candidates.random())
            .performClick()

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        val textAnswer = "answer"
        rule.onNodeWithTag("TextQuestionInputField")
            .performTextInput(textAnswer)

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onAllNodesWithTag("timePicker")
            .assertCountEquals(2)

        rule.onAllNodesWithTag("timePicker")
            .onLast()
            .performClick()

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onAllNodesWithTag("datePicker")
            .assertCountEquals(2)

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onAllNodesWithTag("datePicker")
            .assertCountEquals(2)

        rule.onAllNodesWithTag("timePicker")
            .assertCountEquals(2)

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onAllNodesWithTag("timePicker")
            .assertCountEquals(4)

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onAllNodesWithTag("datePicker")
            .assertCountEquals(4)

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onAllNodesWithTag("datePicker")
            .assertCountEquals(4)

        rule.onAllNodesWithTag("timePicker")
            .assertCountEquals(4)

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        rule.onNodeWithTag("ranking")
            .assertExists()

        getNextButton(rule.activity.getString(R.string.complete))
            .assertExists()
            .performClick()

        assertTrue(completed)
        assertTrue(step.result)

        assertEquals(textAnswer, textInputQuestionModel.input)
    }

    @Test
    fun testTaskCanceled() {
        val test = SurveyTask.Builder(
            id = "task-2",
            revisionId = 2,
            taskId = "survey-sample",
            name = "sample survey",
            description = "test",
            callback = {},
        ).apply {
            addQuestion(multiChoiceQuestionModel1)
        }.build()

        var isCanceled = false
        test.canceled = { isCanceled = true }

        rule.setContent {
            AppTheme(mainLightColors()) {
                test.Render()
            }
        }
        Espresso.pressBack()
        assertTrue(isCanceled)
    }

    @Test
    fun compareTask() {
        assertNotEquals(
            surveyTask,
            SurveyTask.Builder(
                id = "task-3",
                revisionId = 3,
                taskId = "survey-sample",
                name = "sample survey",
                description = "test",
                callback = {},
            ).apply {
                addQuestion(multiChoiceQuestionModel1)
            }.build()
        )
    }

    private fun getNextButton(buttonName: String) =
        rule.onNodeWithText(buttonName)
}
