package healthstack.healthdata.link

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
