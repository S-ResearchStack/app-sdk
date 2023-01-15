package healthstack.kit.survey.question

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.component.ChoiceQuestionComponent
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Slider
import org.junit.Rule
import org.junit.Test

class SliderQuestionTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testSliderQuestion() {
        val component = ChoiceQuestionComponent<ChoiceQuestionModel<Int>>()
        val model = ChoiceQuestionModel(
            id = "slider",
            query = "How was your symptom level for headaches?",
            explanation = "Please tap on the slider to give a rating, from 0 " +
                "being no concern to 10 being extremely concerned.",
            candidates = listOf(0, 10),
            viewType = Slider
        )

        rule.setContent {
            component.Render(model, CallbackCollection())
        }

        rule.onNodeWithText(model.question)
            .assertExists()

        rule.onNodeWithText(model.explanation!!)
            .assertExists()

        rule.onNodeWithText("0")
            .assertExists()

        rule.onNodeWithText("10")
            .assertExists()
    }
}
