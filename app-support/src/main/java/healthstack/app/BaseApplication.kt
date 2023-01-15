package healthstack.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import healthstack.app.pref.AppStage
import healthstack.app.pref.AppStage.Onboarding
import healthstack.app.pref.AppStage.SignUp
import healthstack.app.pref.SettingPreference
import healthstack.app.status.StatusDataType
import healthstack.app.sync.SyncManager
import healthstack.app.task.repository.TaskRepository
import healthstack.app.task.repository.TaskRepositoryImpl
import healthstack.kit.task.onboarding.OnboardingTask
import healthstack.kit.task.signup.SignUpTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    val appStage = runBlocking {
        settingPreference.appStage.first()
    }
    val roomDB: TaskRepository = TaskRepositoryImpl()
    val viewModel = healthstack.app.viewmodel.TaskViewModel(roomDB, settingPreference)

    NavHost(navController = navController, startDestination = appStage.name) {
        composable(AppStage.Main.name) {
            Home(statusList, viewModel)
        }
        composable(Onboarding.name) {
            onboardingTask.callback = {
                scope.launch {
                    settingPreference.setAppStage(SignUp)
                }
                navController.navigate(SignUp.name) {
                    this.launchSingleTop = true
                }
            }
            onboardingTask.Render()
        }
        composable(SignUp.name) {
            SyncManager.initialize(LocalContext.current, healthDataSyncSpecs)
            singUpTask.callback = {
                scope.launch {
                    settingPreference.setAppStage(AppStage.Main)
                    SyncManager.getInstance().startBackgroundSync()
                }
                navController.navigate(AppStage.Main.name) {
                    this.launchSingleTop = true
                }
            }
            singUpTask.Render()
        }
    }
}
