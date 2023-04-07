package healthstack.sample

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import healthstack.kit.auth.SignInProvider.Google
import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.task.onboarding.OnboardingTask
import healthstack.kit.task.onboarding.model.ConsentTextModel
import healthstack.kit.task.onboarding.model.EligibilityIntroModel
import healthstack.kit.task.onboarding.model.EligibilityIntroModel.EligibilityCondition
import healthstack.kit.task.onboarding.model.EligibilityIntroModel.ViewType.Card
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
import healthstack.kit.task.signup.SignUpTask
import healthstack.kit.task.signup.model.RegistrationCompletedModel
import healthstack.kit.task.signup.model.SignUpModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.DropMenu
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.theme.AppColors
import healthstack.kit.theme.mainLightColors
import healthstack.sample.R.drawable
import healthstack.sample.registration.RegistrationModel
import healthstack.sample.registration.RegistrationStep
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    @Singleton
    @Provides
    fun providesAppColors(): AppColors = mainLightColors()

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

    private fun registrationStep(): RegistrationStep =
        RegistrationStep(RegistrationModel("Registration", eligibilityQuestions))

    @Singleton
    @Provides
    fun provideIntroStep(@ApplicationContext context: Context): IntroStep =
        IntroStep(
            "intro-step",
            "Intro-Step",
            intro(context),
            IntroView("Get Started"),
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

    // TODO: add EligibilityCheckerView in builder
    @Singleton
    @Provides
    fun provideEligibilityCheckerStep(@ApplicationContext context: Context): EligibilityCheckerStep =
        EligibilityCheckerStep.Builder("Eligibility")
            .addQuestions(eligibilityQuestions).build()

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

    private fun intro(@ApplicationContext context: Context) = IntroModel(
        id = "intro",
        title = "CardioFlow",
        drawableId = drawable.sample_image_alpha4,
        logoDrawableId = drawable.ic_launcher,
        summaries = listOf(
            drawable.ic_watch to "Wear your\nwatch",
            drawable.ic_alert to "10 min\na day",
            drawable.ic_home_task to "2 surveys\na week"
        ),
        sections = listOf(
            IntroSection(
                "Overview",
                "CardioFlow is a study developed by the University of California, San Francisco.\n\n" +
                    "Through this study, we identify and " +
                    "measure the data of your vital signs and symptom reports.\n\n" +
                    "With your help, we could test our algorithms and " +
                    "develop technology that contributes to preventing cardiovascular diseases in the U.S.",
            ),
            IntroSection(
                "How to participate",
                "Wear the watch as much as possible and take active measurements 3 times a day when notified."
            )
        )
    )

    private fun eligibilityIntro(@ApplicationContext context: Context) = EligibilityIntroModel(
        id = "eligibility",
        title = "Eligibility",
        description = "To begin with, we will ask a few questions " +
            "to make sure that you are eligible to join this study.",
        conditions = eligibilitySections,
        viewType = Card
    )

    private fun eligibilityResult(@ApplicationContext context: Context) = EligibilityResultModel(
        id = "eligibility",
        title = "Eligibility",
        successModel = eligibilitySuccessMessage,
        failModel = eligibilityFailMessage,
    )

    private fun consentText(
        @ApplicationContext context: Context,
    ) = ConsentTextModel(
        id = "consent",
        title = "Informed Consent",
        subTitle = "",
        description = "Read the Terms of Service and Privacy Policy here.",
        checkBoxTexts = listOf(
            "I have read all the information above and I agree to join the study.",
            "I agree to share my data with Samsung.",
            "I agree to share my data with the research assistants in the study."
        )
    )

    private fun signUp() = SignUpModel(
        id = "sign-up-model",
        title = "CardioFlow",
        listOf(Google),
        description = "Thanks for joining the study!\n" +
            "Now please create an account to keep track\n" +
            "of your data and keep it safe.",
        drawableId = drawable.ic_launcher
    )

    private fun registrationCompleted() =
        RegistrationCompletedModel(
            id = "registration-completed-model",
            title = "You are done!",
            buttonText = "Continue",
            description = "Congratulations! Everything is all set for you. " +
                "Now please tap on the button below to start your CardioFlow journey!",
            drawableId = drawable.sample_image_alpha1
        )

    private val eligibilitySections: List<EligibilityIntroModel.EligibilityCondition> = listOf(
        EligibilityCondition(
            "Medical eligibility",
            listOf("Pre-existing condition(s)", "Prescription(s)", "Living in the United States")
        ),
        EligibilityCondition(
            "Basic Profile",
            listOf("Age", "Geographical location", "Devices")
        ),
    )

    private val eligibilitySuccessMessage: ImageArticleModel = ImageArticleModel(
        id = "eligibility",
        title = "Great, You’re in!",
        description = "Congratulations! You are eligible for the study. " +
            "Next, we will need to collect your consent, and you will be ready to go.",
        drawableId = drawable.sample_image_alpha1
    )

    private val eligibilityFailMessage: ImageArticleModel = ImageArticleModel(
        id = "eligibility",
        title = "You’re not eligible for the study.",
        description = "Please check back later and stay tuned for more studies coming soon!",
        drawableId = drawable.sample_image_alpha1
    )

    private val eligibilityQuestions: List<QuestionModel<Any>> = listOf(
        ChoiceQuestionModel(
            "age",
            "What's your age?",
            candidates = (20..50).toList(),
            viewType = DropMenu
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
    ) as List<QuestionModel<Any>>
}
