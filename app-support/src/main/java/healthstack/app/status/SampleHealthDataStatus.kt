package healthstack.app.status

import healthstack.healthdata.link.HealthDataLinkHolder
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS

/**
 * Returns the data key used to extract the heart rate status information.
 * @return The data key.
 */
abstract class SampleHealthDataStatus(private val healthDataName: String) : HealthStatus() {

    /**
     * Retrieves the latest status information for this health data type.
     * @return The latest status information.
     */

    override suspend fun getLatestStatus(): Any? {
        val healthData = HealthDataLinkHolder.getInstance().getHealthData(
            midnight(),
            Instant.now(),
            healthDataName
        )

        if (healthData.data.isEmpty()) return null

        return healthData.data.last()[getDataKey()]
    }

    /**
     * Returns the data key used to extract the status information.
     * @return The data key.
     */
    abstract fun getDataKey(): String

    /**
     * Returns an Instant representing midnight of the current day.
     * @return The Instant representing midnight of the current day.
     */
    private fun midnight() = Instant.now().truncatedTo(DAYS)
}
