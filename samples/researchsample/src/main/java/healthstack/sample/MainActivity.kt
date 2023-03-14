package healthstack.sample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
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

    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val healthDataToDisplay = listOf(HeartRateStatus, SleepSessionStatus, TaskStatus)
        val healthDataSyncSpecs = listOf(
            SyncManager.HealthDataSyncSpec("HeartRate", 15, TimeUnit.MINUTES),
        )

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent()
            intent.action = ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

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
