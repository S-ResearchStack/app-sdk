package healthstack.app

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

/**
* This class contains the test functions for the Base Application.
*
* @property rule the compose rule used for testing
* @property onboardingTask the onboarding task used for testing
* @property signUpTask the sign up task used for testing
*/

class BaseAppTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>
    private val onboardingTask = OnboardingTask(
        id = "onboarding",
        name = "Base Onboarding",
        description = "Onboarding Flow",
        introStep = introStep(),
    )
    /**
     * This function creates an instance of the IntroStep class which is used to show the introduction step in the Onboarding flow.
     *
     * @return the IntroStep object
     */

    private fun introStep() = IntroStep(
        "intro-step",
        "Intro-Step",
        intro(),
        IntroView("Get Started"),
    )
    /**
     * This function creates an instance of the IntroModel class which is used to show the introduction in the Onboarding flow.
     *
     * @return the IntroModel object
     */

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
    /**
     * This variable contains the Sign Up Task that is used in the application.
     */
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
    /**
     * This function tests the Base Application until the Sign Up Task is displayed.
     *
     * @throws [AssertionError] if the assertion on the test case fails
     */

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

    /**
     * This function tests the main functionality of the Base Application.
     *
     * @throws [AssertionError] if the assertion on the test case fails
     */

    @Test
    fun testMain() {
        val healthDataLink = mockk<HealthDataLink>()
        HealthDataLinkHolder.initialize(healthDataLink)
        val firebaseAuth = mockk<FirebaseAuth>()
        val firebaseUser = mockk<FirebaseUser>()
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.displayName } returns "samsung kim"
        every { firebaseUser.email } returns "test@samsung.com"
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

        rule.onNodeWithText("Education")
            .assertExists()
            .performClick()
    }
}
