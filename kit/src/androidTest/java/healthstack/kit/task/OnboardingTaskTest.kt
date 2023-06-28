package healthstack.kit.task

import android.content.Context
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.healthdata.link.HealthDataLink
import healthstack.healthdata.link.HealthDataLinkHolder
import healthstack.kit.R
import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.task.onboarding.OnboardingTask
import healthstack.kit.task.onboarding.model.ConsentTextModel
import healthstack.kit.task.onboarding.model.EligibilityIntroModel
import healthstack.kit.task.onboarding.model.EligibilityIntroModel.EligibilityCondition
import healthstack.kit.task.onboarding.model.EligibilityIntroModel.ViewType
import healthstack.kit.task.onboarding.model.EligibilityIntroModel.ViewType.Card
import healthstack.kit.task.onboarding.model.EligibilityIntroModel.ViewType.Paragraph
import healthstack.kit.task.onboarding.model.EligibilityResultModel
import healthstack.kit.task.onboarding.model.IntroModel
import healthstack.kit.task.onboarding.model.IntroModel.IntroSection
import healthstack.kit.task.onboarding.step.ConsentTextStep
import healthstack.kit.task.onboarding.step.EligibilityCheckerStep
import healthstack.kit.task.onboarding.step.EligibilityIntroStep
import healthstack.kit.task.onboarding.step.EligibilityResultStep
import healthstack.kit.task.onboarding.step.IntroStep
import healthstack.kit.task.onboarding.view.ConsentTextView
import healthstack.kit.task.onboarding.view.EligibilityIntroView
import healthstack.kit.task.onboarding.view.EligibilityResultView
import healthstack.kit.task.onboarding.view.IntroView
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Dropdown
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.theme.AppTheme
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

class OnboardingTaskTest {
    @get:Rule
    val rule = createComposeRule()

    private val delayTime = 1000L

    private val healthLinkMock = mockk<HealthDataLink>()

    private fun onboardingTask(eligibilityIntroViewType: ViewType) = OnboardingTask(
        "onboarding-task",
        "On-Boarding",
        "On-Boarding Flow",
        introStep(),
        eligibilityIntroStep(eligibilityIntroViewType),
        eligibilityCheckerStep(),
        eligibilityResultStep(),
        consentTextStep(),
    )

    private fun introStep() = IntroStep(
        "intro-step",
        "Intro-Step",
        intro(),
        IntroView("Get Started"),
    )

    private fun intro() = IntroModel(
        id = "intro",
        title = "Onboarding Test",
        sections = listOf(
            IntroSection(
                "Overview",
                "Onboarding ",
            ),
            IntroSection(
                "Good for you",
                "Onboarding flow test"
            )
        )
    )

    private fun eligibilityIntroStep(viewType: ViewType): EligibilityIntroStep =
        EligibilityIntroStep(
            "eligibility-intro-step",
            "Eligibility-Intro-Step",
            eligibilityIntro(viewType),
            EligibilityIntroView(),
        )

    private fun eligibilityIntro(viewType: ViewType) = EligibilityIntroModel(
        id = "eligibility",
        title = "Eligibility",
        description = "To begin with, we will ask a few questions " +
            "to make sure that you are eligible to join this study.",
        conditions = eligibilitySections,
        viewType = viewType
    )

    private val eligibilitySections: List<EligibilityCondition> = listOf(
        EligibilityCondition(
            "Medical eligibility",
            listOf("Pre-existing condition(s)", "Prescription(s)")
        ),
        EligibilityCondition(
            "Basic Profile",
            listOf("Age", "Geographical location", "Devices")
        ),
    )

    private fun eligibilityCheckerStep(): EligibilityCheckerStep =
        EligibilityCheckerStep.Builder("Eligibility")
            .addQuestions(eligibilityQuestions).build()

    private val eligibilityQuestions: List<QuestionModel<Any>> = listOf(
        ChoiceQuestionModel(
            "age",
            "What's your age?",
            candidates = (20..50).toList(),
            viewType = Dropdown
        ),
        ChoiceQuestionModel(
            "gender",
            "What's your gender?",
            candidates = listOf("Male", "Female"),
        ),
        ChoiceQuestionModel(
            "hasCardiac",
            "Do you have any existing cardiac conditions?",
            "Examples of cardiac conditions include abnormal heart rhythms, or arrhythmias",
            candidates = listOf("Yes", "No"),
            answer = "Yes"
        ),
        ChoiceQuestionModel(
            "hasWearableDevice",
            "Do you currently own a wearable device?",
            "Examples of wearable devices include Samsung Galaxy Watch 4, Fitbit, OuraRing, etc.",
            candidates = listOf("Yes", "No"),
            answer = "Yes"
        )
    )

    private fun eligibilityResultStep(): EligibilityResultStep =
        EligibilityResultStep(
            "eligibility-result-step",
            "Eligibility-Result-Step",
            eligibilityResult(),
            EligibilityResultView(),
        )

    private fun eligibilityResult() = EligibilityResultModel(
        id = "eligibility",
        title = "Eligibility",
        successModel = eligibilitySuccessMessage,
        failModel = eligibilityFailMessage,
    )

    private val eligibilitySuccessMessage: ImageArticleModel = ImageArticleModel(
        id = "eligibility",
        title = "Great, You’re in!",
        description = "Congratulations! You are eligible for the study. " +
            "Next, we will need to collect your consent, and you will be ready to go.",
        drawableId = R.drawable.sample_image_alpha1
    )

    private val eligibilityFailMessage: ImageArticleModel = ImageArticleModel(
        id = "eligibility",
        title = "You’re not eligible for the study.",
        description = "Please check back later and stay tuned for more studies coming soon!",
        drawableId = R.drawable.sample_image_alpha1
    )

    private fun consentTextStep(): ConsentTextStep =
        ConsentTextStep(
            "consent-text-step",
            "Consent-Text-Step",
            consentText(),
            ConsentTextView()
        )

    private fun consentText() = ConsentTextModel(
        id = "consent",
        title = "Informed Consent",
        subTitle = "",
        description = "Read the Terms of Service and Privacy Policy here.",
        checkBoxTexts = consentList
    )

    private val consentList = listOf(
        "I have read all the information above and I agree to join the study.",
        "I agree to share my data with Samsung.",
        "I agree to share my data with the research assistants in the study."
    )

    @Test
    fun onboardingFlowTaskWithCardView() {
        testWithView(Card)
    }

    @Test
    fun onboardingFlowTaskWithParagraph() {
        testWithView(Paragraph)
    }

    private fun testWithView(viewType: ViewType) {
        HealthDataLinkHolder.initialize(healthLinkMock)
        coJustRun { healthLinkMock.requestPermissions() }
        coEvery { healthLinkMock.hasAllPermissions() } returns true

        val task = onboardingTask(viewType)
        assertFalse(task.isCompleted)

        rule.setContent {
            AppTheme {
                task.Render()
            }
        }

        val context = (rule as AndroidComposeTestRule<*, *>).activity
        checkIntroductionAndPerform()
        checkEligibilityAndPerform(context)
        checkConsentAndPerform(context)
    }

    private fun checkEligibilityAndPerform(context: Context) {
        val flow = listOf(
            { checkEligibilityIntroAndPerform(context) },
            { checkAgeQuestionAndPerform(context) },
            { checkGenderQuestionAndPerform(context) },
            { checkCardiacQuestionAndPerform(context) },
            { checkDeviceQuestionAndPerform(context) },
            { checkSuccessAndPerform(context) },
        )

        flow.forEach { func ->
            func()
            sleep(delayTime)
        }
    }

    private fun checkIntroductionAndPerform() {
        val getStartedButton = rule.onNodeWithText("Get Started")
        getStartedButton.assertExists()
        getStartedButton.performClick()
        sleep(delayTime)
    }

    private fun checkEligibilityIntroAndPerform(context: Context) {
        val checkEligibilityButton = rule.onNodeWithText(context.getString(R.string.eligibility_intro_button_text))
        checkEligibilityButton.assertExists()
        checkEligibilityButton.performClick()
    }

    private fun checkAgeQuestionAndPerform(context: Context) {
        checkQuestion("What's your age?")

        checkButtonEnable(getPreviousButton(context), false)
        val nextButton = getNextButton(context.getString(R.string.next))
        checkButtonEnable(nextButton)

        val dropDown = rule.onNodeWithText("Select One")
        dropDown.assertExists()

        val choiceText = rule.onNodeWithText("23")
        choiceText.assertExists().performClick()
        nextButton.performClick()
    }

    private fun checkGenderQuestionAndPerform(context: Context) {
        checkSingleChoiceQuestionAndPerform(context, "What's your gender?", "Male")
    }

    private fun checkCardiacQuestionAndPerform(context: Context) {
        checkSingleChoiceQuestionAndPerform(
            context,
            "Do you have any existing cardiac conditions?",
            "Yes"
        )
    }

    private fun checkDeviceQuestionAndPerform(context: Context) {
        checkSingleChoiceQuestionAndPerform(
            context,
            "Do you currently own a wearable device?",
            "Yes",
            context.getString(R.string.complete)
        )
    }

    private fun checkSingleChoiceQuestionAndPerform(
        context: Context,
        query: String,
        choice: String,
        nextButtonName: String? = null,
    ) {
        checkQuestion(query)

        checkButtonEnable(getPreviousButton(context))

        val nextButton = getNextButton(nextButtonName ?: context.getString(R.string.next))
        checkButtonEnable(nextButton)

        selectOne(choice)

        nextButton.performClick()
    }

    private fun selectOne(choice: String) {
        rule.onNodeWithTag(choice)
            .assertExists().performClick()
    }

    private fun checkQuestion(query: String) {
        rule.onNodeWithText(query).assertExists()
    }

    private fun checkSuccessAndPerform(context: Context) {
        rule.onNodeWithText(eligibilitySuccessMessage.title)
            .assertExists()

        rule.onNodeWithText(context.getString(R.string.continuous))
            .performClick()
    }

    private fun checkConsentAndPerform(context: Context) {
        val joinStudyButton = rule.onNodeWithText(context.getString(R.string.join_study))
        joinStudyButton.assertExists().assertIsNotEnabled()

        consentList.forEach { consentText ->
            rule.onNodeWithTag(consentText).performClick()
        }

        rule.onNodeWithText("Tap to sign.").performClick()

        // TODO: landscape orientation is not supported yet
        /* rule.onNodeWithTag("signatureView").performTouchInput {
            swipeRight()
            swipeLeft()
        }
        rule.onNodeWithTag("signatureDoneButton").performClick()

        joinStudyButton.assertIsEnabled().performClick() */

        sleep(delayTime)
    }

    private fun getPreviousButton(context: Context) = rule.onNodeWithText(context.getString(R.string.previous))

    private fun getNextButton(buttonName: String) =
        rule.onNodeWithText(buttonName)

    private fun checkButtonEnable(button: SemanticsNodeInteraction, enabled: Boolean = true) {
        button.assertExists()
        if (enabled)
            button.assertIsEnabled()
        else button.assertIsNotEnabled()
    }
}
