package healthstack.kit.task.activity.view.common

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.R
import healthstack.kit.R.drawable
import healthstack.kit.task.activity.model.ColorWordChallengeIntroModel
import healthstack.kit.task.activity.model.ColorWordChallengeResultModel
import healthstack.kit.task.activity.model.GaitAndBalanceGMeasureModel
import healthstack.kit.task.activity.model.GaitAndBalanceIntroModel
import healthstack.kit.task.activity.model.GaitAndBalanceResultModel
import healthstack.kit.task.activity.model.GuidedBreathingIntroModel
import healthstack.kit.task.activity.model.GuidedBreathingResultModel
import healthstack.kit.task.activity.model.MobileSpirometryResultModel
import healthstack.kit.task.activity.model.RangeOfMotionIntroModel
import healthstack.kit.task.activity.model.RangeOfMotionResultModel
import healthstack.kit.task.activity.model.ReactionTimeIntroModel
import healthstack.kit.task.activity.model.ReactionTimeResultModel
import healthstack.kit.task.activity.model.SpeechRecognitionResultModel
import healthstack.kit.task.activity.model.SustainedPhonationResultModel
import healthstack.kit.task.activity.model.TappingSpeedIntroModel
import healthstack.kit.task.activity.model.TappingSpeedResultModel
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class SimpleActivityViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun simpleActivityViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = SimpleViewActivityModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = R.drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun colorWordChallengeViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = ColorWordChallengeIntroModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = R.drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun colorWordChallengeResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = ColorWordChallengeResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun gaitAndBalanceGViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = GaitAndBalanceGMeasureModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun gaitAndBalanceIntroViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = GaitAndBalanceIntroModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun gaitAndBalanceResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = GaitAndBalanceResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun guidedBreathingIntroViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = GuidedBreathingIntroModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun guidedBreathingResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                val model = GuidedBreathingResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun mobileSpirometryResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = MobileSpirometryResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun rangeOfMotionIntroViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = RangeOfMotionIntroModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun rangeOfMotionResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = RangeOfMotionResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun reactionTimeIntroViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = ReactionTimeIntroModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun reactionTimeResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = ReactionTimeResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun speechRecognitionResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = SpeechRecognitionResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun sustainedPhonationResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = SustainedPhonationResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun tappingSpeedIntroViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = TappingSpeedIntroModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun tappingSpeedResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = TappingSpeedResultModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    drawableId = drawable.ic_call
                )

                SimpleActivityView<SimpleViewActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }
}
