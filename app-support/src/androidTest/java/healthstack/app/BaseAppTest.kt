package healthstack.app

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.firebase.auth.FirebaseAuth
import healthstack.app.pref.AppStage
import healthstack.app.pref.AppStage.Onboarding
import healthstack.app.pref.SettingPreference
import healthstack.app.status.HeartRateStatus
import healthstack.app.status.SleepSessionStatus
import healthstack.app.status.TaskStatus
import healthstack.app.task.db.TaskRoomDatabase
import healthstack.healthdata.link.HealthData
import healthstack.healthdata.link.HealthDataLink
import healthstack.healthdata.link.HealthDataLinkHolder
import healthstack.kit.auth.SignInProvider.Basic
import healthstack.kit.task.onboarding.OnboardingTask
import healthstack.kit.task.onboarding.model.IntroModel
import healthstack.kit.task.onboarding.model.IntroModel.IntroSection
import healthstack.kit.task.onboarding.step.IntroStep
import healthstack.kit.task.onboarding.view.IntroView
import healthstack.kit.task.signup.SignUpTask
import healthstack.kit.task.signup.model.RegistrationCompletedModel
import healthstack.kit.task.signup.model.SignUpModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class BaseAppTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>
    private val onboardingTask = OnboardingTask(
        id = "onboarding",
        name = "Base Onboarding",
        description = "Onboarding Flow",
        introStep = introStep(),
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

    private val signUpTask = SignUpTask(
        "signup-task",
        "Sign Up",
        "",
        SignUpModel(
            id = "sign-up-model",
            title = "SignUpTask",
            listOf(Basic),
            description = "Thanks for joining the study!"
        ),
        RegistrationCompletedModel(
            id = "registration-completed-model",
            title = "You are done!",
            buttonText = "Continue",
            description = "Congratulations! Everything is all set for you. " +
                "Now please tap on the button below to start your CardioFlow journey!",
            drawableId = R.drawable.ic_task
        ),
    )

    @Test
    fun testBaseAppUntilSignUp() {
        val pref = SettingPreference(rule.activity)
        val firebaseAuth = mockk<FirebaseAuth>()
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth

        runBlocking {
            pref.setAppStage(Onboarding)
        }

        rule.setContent {
            TaskRoomDatabase.initialize(LocalContext.current)
            BaseApplication(
                onboardingTask,
                signUpTask,
                listOf(HeartRateStatus, SleepSessionStatus, TaskStatus),
                listOf()
            )
        }

        rule.onNodeWithText("Get Started")
            .assertExists()
            .assertIsEnabled()
    }

    @Test
    fun testMain() {
        val healthDataLink = mockk<HealthDataLink>()
        HealthDataLinkHolder.initialize(healthDataLink)

        coEvery { healthDataLink.getHealthData(any(), any(), any()) } returns HealthData("healthdata", emptyList())

        val pref = SettingPreference(rule.activity)
        runBlocking {
            pref.setAppStage(AppStage.Home)
        }
        rule.setContent {
            TaskRoomDatabase.initialize(LocalContext.current)
            BaseApplication(
                onboardingTask,
                signUpTask,
                listOf(HeartRateStatus, SleepSessionStatus, TaskStatus),
                listOf()
            )
        }

        rule.onNodeWithText(LocalDate.now().dayOfMonth.toString())
            .assertExists()
    }
}
