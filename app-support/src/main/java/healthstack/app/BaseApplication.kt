package healthstack.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import healthstack.app.pref.AppStage
import healthstack.app.pref.AppStage.Education
import healthstack.app.pref.AppStage.Home
import healthstack.app.pref.AppStage.Onboarding
import healthstack.app.pref.AppStage.Profile
import healthstack.app.pref.AppStage.Settings
import healthstack.app.pref.AppStage.SignUp
import healthstack.app.pref.AppStage.StudyInformation
import healthstack.app.pref.SettingPreference
import healthstack.app.status.StatusDataType
import healthstack.app.sync.FileSyncManager
import healthstack.app.sync.SyncManager
import healthstack.app.sync.SyncWearDataManager
import healthstack.app.task.repository.TaskRepository
import healthstack.app.task.repository.TaskRepositoryImpl
import healthstack.kit.info.MyProfileView
import healthstack.kit.info.SettingsView
import healthstack.kit.info.StudyInfoView
import healthstack.kit.info.publication.PdfPublication
import healthstack.kit.info.publication.content.TextBlock
import healthstack.kit.task.onboarding.OnboardingTask
import healthstack.kit.task.signup.SignUpTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

/**
 * Composable function representing the entire application.
 *
 * @param onboardingTask the onboarding task to be performed when the app is launched.
 * @param singUpTask the sign up task to be performed after the onboarding task is completed.
 * @param statusList the list of status data types to be displayed in the UI.
 * @param healthDataSyncSpecs the list of health data sync specifications to be used by the SyncManager.
 */

@Composable
fun BaseApplication(
    onboardingTask: OnboardingTask,
    singUpTask: SignUpTask,
    statusList: List<StatusDataType>,
    healthDataSyncSpecs: List<SyncManager.HealthDataSyncSpec>,
) {
    val settingPreference = SettingPreference(LocalContext.current)
    Main(
        settingPreference,
        onboardingTask,
        singUpTask,
        statusList,
        healthDataSyncSpecs
    )
}

/**
 * The main function of the application.
 *
 * @param settingPreference the shared preferences object for the application.
 * @param onboardingTask the onboarding task to be performed when the app is launched.
 * @param singUpTask the sign up task to be performed after the onboarding task is completed.
 * @param statusList the list of status data types to be displayed in the UI.
 * @param healthDataSyncSpecs the list of health data sync specifications to be used by the SyncManager.
 */
@Composable
private fun Main(
    settingPreference: SettingPreference,
    onboardingTask: OnboardingTask,
    singUpTask: SignUpTask,
    statusList: List<StatusDataType>,
    healthDataSyncSpecs: List<SyncManager.HealthDataSyncSpec>,
) {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val initialAppStage = runBlocking {
        settingPreference.appStage.first()
    }

    val roomDB: TaskRepository = TaskRepositoryImpl()
    val viewModel = healthstack.app.viewmodel.TaskViewModel(roomDB, settingPreference)

    val changeNavigation = { stage: AppStage ->
        scope.launch {
            settingPreference.setAppStage(stage)
        }
        navController.navigate(stage.name) {
            this.launchSingleTop = true
            popUpTo(0)
        }
    }

    NavHost(navController = navController, startDestination = initialAppStage.name) {
        composable(Home.name) {
            Home(statusList, viewModel, changeNavigation)
        }
        composable(Profile.name) {
            MyProfileView(onClickBack = { changeNavigation(Home) }).Render()
        }
        composable(StudyInformation.name) {
            StudyInfoView(onClickBack = { changeNavigation(Home) }).Render()
        }
        composable(Settings.name) {
            SettingsView(
                onClickBack = { changeNavigation(Home) },
                initialize = { changeNavigation(Onboarding) }
            ).Render()
        }
        composable(Education.name) {
            EducationView(
                changeNavigation,
                listOf(
                    PdfPublication(
                        "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
                        "Heart Health",
                        "How to Prevent a Heart Stroke?",
                        "10 min read",
                        listOf(
                            TextBlock(
                                "To prevent a stroke, prioritize a healthy lifestyle. "
                            )
                        )
                    )
                )
            ).Render()
        }
        composable(Onboarding.name) {
            onboardingTask.callback = { changeNavigation(SignUp) }
            onboardingTask.Render()
        }
        composable(SignUp.name) {
            SyncManager.initialize(LocalContext.current, healthDataSyncSpecs)
            SyncWearDataManager.initialize(LocalContext.current, 15, TimeUnit.MINUTES)
            FileSyncManager.initialize(LocalContext.current, 15)

            singUpTask.callback = {
                scope.launch {
                    SyncManager.getInstance().startBackgroundSync()
                    SyncWearDataManager.getInstance().startBackgroundSync()
                    FileSyncManager.getInstance().startBackgroundSync()
                }
                changeNavigation(Home)
            }
            singUpTask.Render()
        }
    }
}
