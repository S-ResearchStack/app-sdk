package researchstack.presentation.initiate.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import researchstack.presentation.screen.insight.SettingScreen
import researchstack.presentation.screen.insight.StudyPermissionSettingScreen
import researchstack.presentation.screen.insight.StudyStatusScreen
import researchstack.presentation.screen.main.MainScreen
import researchstack.presentation.screen.study.EligibilityFailScreen
import researchstack.presentation.screen.study.StudyCodeInputScreen
import researchstack.presentation.screen.study.StudyEligibilityScreen
import researchstack.presentation.screen.study.StudyInformationScreen
import researchstack.presentation.screen.study.StudyInformedConsentScreen
import researchstack.presentation.screen.task.TaskScreen
import researchstack.presentation.screen.welcome.WelcomeScreen

private val mainRouteName = "${Route.Main.name}/{page}"

@Composable
fun Router(navController: NavHostController, startRoute: Route, askedPage: Int) {
    val startDestination = startRoute.name

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Route.Welcome.name) {
            WelcomeScreen()
        }
        composable(Route.Main.name) {
            MainScreen(askedPage)
        }
        composable(
            mainRouteName,
            arguments = listOf(navArgument("page") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val page = navBackStackEntry.arguments?.getInt("page") ?: 0
            MainScreen(page)
        }
        composable(Route.StudyCode.name) {
            StudyCodeInputScreen()
        }
        composable(Route.StudyInformation.name) {
            StudyInformationScreen()
        }
        composable(
            "${Route.StudyEligibility.name}/{studyId}",
            arguments = listOf(navArgument("studyId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("studyId")?.let {
                StudyEligibilityScreen(it)
            }
        }
        composable(Route.InformedConsent.name) {
            StudyInformedConsentScreen()
        }
        composable(Route.EligibilityFailed.name) {
            EligibilityFailScreen()
        }
        composable(
            "${Route.StudyPermissionSetting.name}/{studyId}",
            arguments = listOf(navArgument("studyId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("studyId")?.let {
                StudyPermissionSettingScreen(it)
            }
        }
        composable("${Route.Task.name}/{taskId}") { navBackStackEntry ->
            val taskId = navBackStackEntry.arguments?.getString("taskId")?.toInt()
            taskId?.let {
                TaskScreen(it)
            }
        }
        composable("${Route.StudyStatus.name}/{status}") { navBackStackEntry ->
            val status = navBackStackEntry.arguments?.getString("status")?.toInt()
            status?.let {
                StudyStatusScreen(it)
            }
        }
        composable(Route.Settings.name) {
            SettingScreen()
        }
    }
}
