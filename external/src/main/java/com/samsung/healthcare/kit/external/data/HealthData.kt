package com.samsung.healthcare.kit.external.data

data class HealthData(
    val type: String,
    // To fit API spec (healthcare research platform)
    val data: List<Map<String, Any>>,
) {
    companion object {
        const val TIME_KEY = "time"
        const val START_TIME_KEY = "startTime"
        const val END_TIME_KEY = "endTime"
    }
}
