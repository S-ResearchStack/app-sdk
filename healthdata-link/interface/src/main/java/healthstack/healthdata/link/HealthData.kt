package healthstack.healthdata.link

/**
 * Transfer object created to transmit health data according to Backend API Spec.
 *
 * @property type Type of health data. (ex) BloodPressure, HeartRate, SleepSession
 * @property data List of actual data collected.
 */
data class HealthData(
    val type: String,
    val data: List<Map<String, Any>>,
) {
    companion object {
        const val TIME_KEY = "time"
        const val START_TIME_KEY = "startTime"
        const val END_TIME_KEY = "endTime"
    }
}
