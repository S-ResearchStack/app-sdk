package com.samsung.healthcare.kit.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.ConsentTextModel
import org.junit.Rule
import org.junit.Test

class ConsentViewTest {
    @get:Rule
    val rule = createComposeRule()

    private fun createConsentTextModel(
        id: String = "consent-text-model",
        title: String = "Informed Consent",
        subTitle: String = "",
        description: String = "Read the Terms of Service and Privacy Policy here.",
        checkBoxTexts: List<String> = listOf(
            "I have read all the information above and I agree to join the study.",
            "I agree to share my data with Samsung.",
            "I agree to share my data with the research assistants in the study."
        ),
    ): ConsentTextModel = ConsentTextModel(
        id, title, subTitle, description, checkBoxTexts
    )

    private fun createConsentTextView(
        buttonText: String = "Done",
    ): ConsentTextView = ConsentTextView(buttonText)

    @Test
    fun titleRenderSuccess() {
        val consentTextModel = createConsentTextModel(title = "testTitle")
        val consentTextView = createConsentTextView()
        val callbackCollection = CallbackCollection()

        rule.setContent {
            consentTextView.Render(consentTextModel, callbackCollection, null)
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()
    }
}
