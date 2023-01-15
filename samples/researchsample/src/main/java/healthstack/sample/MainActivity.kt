package healthstack.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import healthstack.app.BaseApplication
import healthstack.app.status.HeartRateStatus
import healthstack.app.status.SleepSessionStatus
import healthstack.app.status.TaskStatus
import healthstack.app.sync.SyncManager
import healthstack.healthdata.link.HealthDataLinkHolder
import healthstack.healthdata.link.healthconnect.HealthConnectAdapter
import healthstack.kit.task.onboarding.OnboardingTask
import healthstack.kit.task.signup.SignUpTask
import healthstack.kit.theme.AppColors
import healthstack.kit.theme.AppTheme
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appColors: AppColors

    @Inject
    lateinit var onboardingTask: OnboardingTask

    @Inject
    lateinit var signUpTask: SignUpTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val healthDataToDisplay = listOf(HeartRateStatus, SleepSessionStatus, TaskStatus)
        val healthDataSyncSpecs = listOf(
            SyncManager.HealthDataSyncSpec("HeartRateSeries", 15, TimeUnit.MINUTES),
        )

        (HealthDataLinkHolder.getInstance() as HealthConnectAdapter).createLauncher(this)

        setContent {
            Surface {
                AppTheme(appColors) {
                    BaseApplication(
                        onboardingTask,
                        signUpTask,
                        healthDataToDisplay,
                        healthDataSyncSpecs
                    )
                }
            }
        }
    }
}
