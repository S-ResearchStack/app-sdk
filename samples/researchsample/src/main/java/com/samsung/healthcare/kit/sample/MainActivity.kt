package com.samsung.healthcare.kit.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.toArgb
import com.google.android.libraries.healthdata.HealthDataService
import com.google.android.libraries.healthdata.data.IntervalDataTypes.SLEEP_SESSION
import com.google.android.libraries.healthdata.data.SampleDataTypes.HEART_RATE
import com.samsung.healthcare.kit.app.BaseApplication
import com.samsung.healthcare.kit.app.TaskDataType.Companion.TASK_DATA_TYPE
import com.samsung.healthcare.kit.external.background.SyncManager
import com.samsung.healthcare.kit.external.network.ResearchPlatformAdapter
import com.samsung.healthcare.kit.external.source.HealthPlatformAdapter
import com.samsung.healthcare.kit.repository.TaskRoomDatabase
import com.samsung.healthcare.kit.task.OnboardingTask
import com.samsung.healthcare.kit.task.SignUpTask
import com.samsung.healthcare.kit.theme.AppColors
import com.samsung.healthcare.kit.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
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

        val healthDataRequired = listOf("HeartRate", "Steps", "SleepSession")
        val healthDataToDisplay = listOf(HEART_RATE, SLEEP_SESSION, TASK_DATA_TYPE)
        val healthDataSyncSpecs = listOf(
            SyncManager.HealthDataSyncSpec("HeartRate", 15, TimeUnit.MINUTES),
            SyncManager.HealthDataSyncSpec("Steps", 1, TimeUnit.DAYS),
            SyncManager.HealthDataSyncSpec("SleepSession", 1, TimeUnit.DAYS)
        )

        HealthPlatformAdapter.initialize(HealthDataService.getClient(this), healthDataRequired)
        ResearchPlatformAdapter.initialize(
            this.getString(R.string.research_platform_endpoint),
            this.getString(R.string.research_project_id)
        )
        TaskRoomDatabase.initialize(this)

        setContent {
            Surface {
                AppTheme(appColors) {
                    this.window.statusBarColor = AppTheme.colors.primary.toArgb()
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
