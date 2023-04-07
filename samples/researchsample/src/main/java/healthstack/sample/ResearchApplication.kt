package healthstack.sample

import android.app.Application
import androidx.health.connect.client.HealthConnectClient
import dagger.hilt.android.HiltAndroidApp
import healthstack.app.task.db.TaskRoomDatabase
import healthstack.backend.integration.BackendFacadeHolder
import healthstack.backend.integration.adapter.HealthStackBackendAdapter
import healthstack.healthdata.link.HealthDataLinkHolder
import healthstack.healthdata.link.healthconnect.HealthConnectAdapter
import healthstack.kit.sensor.SensorUtils

@HiltAndroidApp
class ResearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val healthDataRequired = listOf("HeartRate", "SleepSession")

        HealthDataLinkHolder.initialize(
            HealthConnectAdapter(
                healthDataRequired,
                HealthConnectClient.getOrCreate(this)
            )
        )

        BackendFacadeHolder.initialize(
            HealthStackBackendAdapter.initialize(
                this.getString(R.string.research_platform_endpoint),
                this.getString(R.string.research_project_id)
            ).let { HealthStackBackendAdapter.getInstance() }
        )

        SensorUtils.initialize(this)

        TaskRoomDatabase.initialize(this)
    }
}
