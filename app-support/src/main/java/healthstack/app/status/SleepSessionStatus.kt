package healthstack.app.status

import healthstack.app.R
import healthstack.app.viewmodel.SleepSessionStatusViewModel
import healthstack.healthdata.link.HealthDataLinkHolder
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS
import kotlin.math.roundToInt

/**
 * An object representing the status of sleep sessions.
 */
object SleepSessionStatus : HealthStatus() {
    /**
     * Returns the icon resource ID for sleep sessions.
     * @return The icon resource ID.
     */
    override fun getIcon(): Int = R.drawable.ic_sleep

    /**
     * Returns the unit string for sleep sessions.
     * @return The unit string.
     */
    override fun getUnitString(): String = " hrs"

    /**
     * Returns an Instant representing midnight of the current day.
     * @return The Instant representing midnight of the current day.
     */
    override suspend fun getLatestStatus(): Any? = getLatestSleepSession()

    /**
     * Returns an Instant representing midnight of the current day.
     * @return The Instant representing midnight of the current day.
     */
    private suspend fun getLatestSleepSession(): Any? {
        val yesterday = Instant.now().truncatedTo(DAYS).minus(1, DAYS)
        val healthData = HealthDataLinkHolder.getInstance().getHealthData(
            yesterday,
            Instant.now(),
            "SleepSession"
        )

        if (healthData.data.isEmpty()) return null
        val latestSleep = healthData.data.last()
        val startTime = latestSleep["startTime"] as? Instant ?: return null
        val endTime = latestSleep["endTime"] as? Instant ?: return null

        val sleepDuration = Duration.between(
            startTime,
            endTime
        ).toMinutes() / 60.0

        return (sleepDuration * 10).roundToInt() / 10.0
    }

    override fun toViewModel() = SleepSessionStatusViewModel
}
