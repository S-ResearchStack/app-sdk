package healthstack.app.status

import healthstack.healthdata.link.HealthDataLinkHolder
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS

abstract class SampleHealthDataStatus(private val healthDataName: String) : StatusDataType() {

    override suspend fun getLatestStatus(): Any? {
        val healthData = HealthDataLinkHolder.getInstance().getHealthData(
            midnight(),
            Instant.now(),
            healthDataName
        )

        if (healthData.data.isEmpty()) return null

        return healthData.data.last()[getDataKey()]
    }

    abstract fun getDataKey(): String

    private fun midnight() = Instant.now().truncatedTo(DAYS)
}
