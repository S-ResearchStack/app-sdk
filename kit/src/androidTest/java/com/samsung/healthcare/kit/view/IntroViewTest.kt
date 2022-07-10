package com.samsung.healthcare.kit.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.IntroModel
import com.samsung.healthcare.kit.model.IntroModel.IntroSection
import org.junit.Rule
import org.junit.Test

class IntroViewTest {
    @get:Rule
    val rule = createComposeRule()

    // For Readability
    private fun createIntroModel(
        id: String = "intro-model",
        title: String = "SleepCare",
        drawableId: Int = R.drawable.sample_image_alpha3,
        logoDrawableId: Int = R.drawable.ic_sample_icon,
        summaries: List<Pair<Int, String>> = listOf(
            R.drawable.ic_watch to "Wear your watch",
        ),
        introSections: List<IntroSection> = listOf(
            IntroSection("subTitle", "Description")
        ),
    ): IntroModel =
        IntroModel(
            id, title, drawableId, logoDrawableId, summaries, introSections
        )

    private fun createIntroView(
        bottomBarText: String = "TestBottomBar",
    ): IntroView =
        IntroView(bottomBarText)

    private fun createCallbackCollection(): CallbackCollection = CallbackCollection()

    @Test
    fun titleRenderSuccess() {
        val introModel = createIntroModel(title = "testTitle")
        val introView = createIntroView()
        val callbackCollection = createCallbackCollection()

        rule.setContent {
            introView.Render(introModel, callbackCollection, null)
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()
    }

    @Test
    fun bottomBarRenderSuccess() {
        val introModel = createIntroModel()
        val introView = createIntroView()
        val callbackCollection = createCallbackCollection()

        rule.setContent {
            introView.Render(introModel, callbackCollection, null)
        }

        rule.onNodeWithText("TestBottomBar").assertExists()
        rule.onNodeWithText("WrongBottomBar").assertDoesNotExist()
    }
}
