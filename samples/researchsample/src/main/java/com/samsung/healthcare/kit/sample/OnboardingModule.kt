package com.samsung.healthcare.kit.sample

import android.content.Context
import com.samsung.healthcare.kit.auth.SignInProvider
import com.samsung.healthcare.kit.external.datastore.MetaDataStore
import com.samsung.healthcare.kit.model.ConsentTextModel
import com.samsung.healthcare.kit.model.EligibilityCheckerModel
import com.samsung.healthcare.kit.model.EligibilityIntroModel
import com.samsung.healthcare.kit.model.EligibilityResultModel
import com.samsung.healthcare.kit.model.ImageArticleModel
import com.samsung.healthcare.kit.model.IntroModel
import com.samsung.healthcare.kit.model.RegistrationCompletedModel
import com.samsung.healthcare.kit.model.SignUpModel
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel.ViewType.DropMenu
import com.samsung.healthcare.kit.sample.registration.RegistrationModel
import com.samsung.healthcare.kit.sample.registration.RegistrationStep
import com.samsung.healthcare.kit.step.ConsentTextStep
import com.samsung.healthcare.kit.step.EligibilityCheckerStep
import com.samsung.healthcare.kit.step.EligibilityIntroStep
import com.samsung.healthcare.kit.step.EligibilityResultStep
import com.samsung.healthcare.kit.step.IntroStep
import com.samsung.healthcare.kit.step.sub.QuestionSubStep
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.task.OnboardingTask
import com.samsung.healthcare.kit.task.SignUpTask
import com.samsung.healthcare.kit.theme.AppColors
import com.samsung.healthcare.kit.theme.darkColors
import com.samsung.healthcare.kit.view.ConsentTextView
import com.samsung.healthcare.kit.view.EligibilityCheckerView
import com.samsung.healthcare.kit.view.EligibilityIntroView
import com.samsung.healthcare.kit.view.EligibilityResultView
import com.samsung.healthcare.kit.view.IntroView
import com.samsung.healthcare.kit.view.component.ChoiceQuestionComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    @Singleton
    @Provides
    fun providesAppColors(): AppColors = darkColors()

    @Singleton
    @Provides
    fun provideOnboardingTask(
        introStep: IntroStep,
        eligibilityIntroStep: EligibilityIntroStep,
        eligibilityCheckerStep: EligibilityCheckerStep,
        eligibilityResultStep: EligibilityResultStep,
        consentTextStep: ConsentTextStep,
    ): OnboardingTask =
        OnboardingTask(
            "onboarding-task",
            "Sample-App-On-Boarding",
            "Introduce the project and determine whether the user is able to participate in this project or not.",
            introStep,
            eligibilityIntroStep,
            eligibilityCheckerStep,
            eligibilityResultStep,
            consentTextStep,
        )

    @Singleton
    @Provides
    fun provideSignUpTask(): SignUpTask =
        SignUpTask(
            "signup-task",
            "Sign Up",
            "",
            signUp(),
            registrationCompleted(),
            registrationStep()
        )

    fun registrationStep(): RegistrationStep =
        RegistrationStep(
            RegistrationModel(
                "Registration",
                listOf(
                    ChoiceQuestionModel(
                        "age",
                        "1. what's youre age",
                        candidates = (20..50).toList(),
                        viewType = DropMenu
                    ),

                    ChoiceQuestionModel(
                        "gender",
                        "2. What's your gender?",
                        candidates = listOf("Male", "Female"),
                    ),
                )
            )
        )

    @Singleton
    @Provides
    fun provideIntroStep(@ApplicationContext context: Context): IntroStep =
        IntroStep(
            "intro-step",
            "Intro-Step",
            intro(context),
            IntroView(),
        )

    @Singleton
    @Provides
    fun provideEligibilityIntroStep(@ApplicationContext context: Context): EligibilityIntroStep =
        EligibilityIntroStep(
            "eligibility-intro-step",
            "Eligibility-Intro-Step",
            eligibilityIntro(context),
            EligibilityIntroView(),
        )

    @Singleton
    @Provides
    fun provideEligibilityCheckerStep(@ApplicationContext context: Context): EligibilityCheckerStep =
        EligibilityCheckerStep(
            "eligibility-checker-step",
            "Eligibility-Checker-Step",
            eligibilityChecker(context),
            EligibilityCheckerView(),
            SubStepHolder(
                "sub-step-holder",
                "Sub-Step-Holder",
                questionnaireSubSteps
            )
        )

    @Singleton
    @Provides
    fun provideEligibilityResultStep(@ApplicationContext context: Context): EligibilityResultStep =
        EligibilityResultStep(
            "eligibility-result-step",
            "Eligibility-Result-Step",
            eligibilityResult(context),
            EligibilityResultView(),
        )

    @Singleton
    @Provides
    fun provideConsentTextStep(
        @ApplicationContext context: Context,
    ): ConsentTextStep =
        ConsentTextStep(
            "consent-text-step",
            "Consent-Text-Step",
            consentText(context),
            ConsentTextView()
        )

    @Singleton
    @Provides
    fun providePreferencesStore(@ApplicationContext context: Context): MetaDataStore =
        MetaDataStore(context)

    private fun intro(@ApplicationContext context: Context) = IntroModel(
        id = "intro-model",
        title = "SleepCare",
        drawableId = R.drawable.sample_image4,
        logoDrawableId = R.drawable.ic_sample_icon,
        summaries = listOf(
            R.drawable.ic_watch to "Wear your watch",
            R.drawable.ic_clock to "10 min a day",
            R.drawable.ic_alarm to "2 surveys a week"
        ),
        descriptions = listOf(
            "Description" to context.getString(R.string.lorem_ipsum),
            "Description2" to context.getString(R.string.lorem_ipsum)
        )
    )

    private fun eligibilityIntro(@ApplicationContext context: Context) = EligibilityIntroModel(
        id = "eligibility-intro-model",
        title = "Eligibility-Intro-Title",
        description = context.getString(R.string.lorem_ipsum_short),
        conditions = eligibilitySections,
        drawableId = R.drawable.sample_image1,
        viewType = EligibilityIntroModel.ViewType.Card
    )

    private fun eligibilityChecker(@ApplicationContext context: Context) = EligibilityCheckerModel(
        id = "eligibility-checker-model",
        title = "Eligibility-Checker-Title",
        drawableId = R.drawable.sample_image1,
    )

    private fun eligibilityResult(@ApplicationContext context: Context) = EligibilityResultModel(
        id = "eligibility-result-model",
        title = "Eligibility-Result-Title",
        drawableId = R.drawable.sample_image1,
        successModel = eligibilitySuccessMessage,
        failModel = eligibilityFailMessage,
    )

    private fun consentText(
        @ApplicationContext context: Context,
    ) = ConsentTextModel(
        id = "consent-text-model",
        title = "Informed Consent",
        subTitle = "",
        description = "Read the Terms of Service and Privacy Policy here.",
        checkBoxTexts = listOf(
            "I have read all the information above and I agree to join the study.",
            "I agree to share my data with Samsung.",
            "I agree to share my data with the research assistants in the study."
        ),
    )

    private fun signUp() = SignUpModel(
        id = "sign-up-model",
        title = "SleepCare",
        listOf(SignInProvider.Google),
        description = "Thanks for joining the study! Now please create an account to keep track of your data and keep it safe.",
        drawableId = R.drawable.ic_sample_icon
    )

    private fun registrationCompleted() =
        RegistrationCompletedModel(
            id = "registration-completed-model",
            title = "You are done!",
            buttonText = "Continue",
            description = "Congratulations! Everything is all set for you. Now please tap on the button below to start your SleepCare journey!",
            drawableId = R.drawable.sample_image4
        )

    private val eligibilitySections: List<EligibilityIntroModel.EligibilityCondition> = listOf(
        EligibilityIntroModel.EligibilityCondition(
            "Medical eligibility",
            listOf("Condition(s)", "Prescription(s)", "Living in Europe")
        ),
        EligibilityIntroModel.EligibilityCondition(
            "Basic Profile",
            listOf("18 and over", "Living in Europe")
        ),
    )

    private val eligibilitySuccessMessage: ImageArticleModel = ImageArticleModel(
        "success-message",
        "Great! You’re in!",
        "Congratulations! You are eligible for the study.",
        drawableId = R.drawable.sample_image2
    )

    private val eligibilityFailMessage: ImageArticleModel = ImageArticleModel(
        "fail-message",
        "You are not eligible for the study.",
        "Check back later and stay tuned for more studies coming soon!",
        drawableId = R.drawable.sample_image3
    )

    private val questionnaireSubSteps: List<QuestionSubStep<*, *>> = listOf(
        QuestionSubStep(
            "question-1",
            "Question-Name-1",
            ChoiceQuestionModel(
                "choice-question-model-1",
                "Do you have any existing cardiac conditions?",
                "Examples of cardiac conditions include abnormal heart rhythms, or arrhythmias",
                candidates = listOf("Yes", "No"),
                answer = "Yes"
            ),
            ChoiceQuestionComponent(),
        ),
        QuestionSubStep(
            "question-2",
            "Question-Name-2",
            ChoiceQuestionModel(
                "choice-question-model-2",
                "Do you currently own a wearable device?",
                "Examples of wearable devices include Samsung Galaxy Watch 4, Fitbit, OuraRing, etc.",
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
                "test page?",
                candidates = listOf("Yes", "No"),
                answer = "Yes"
            ),
            ChoiceQuestionComponent(),
        )
    )
}
