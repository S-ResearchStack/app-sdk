package healthstack.healthdata.link

import java.time.Instant

interface HealthDataLink {
    suspend fun hasAllPermissions(): Boolean

    suspend fun requestPermissions()

    suspend fun getHealthData(
        startTime: Instant,
        endTime: Instant,
        healthDataTypeName: String,
    ): HealthData

    suspend fun getChangesToken(healthDataTypeName: String): String

    suspend fun getChanges(
        token: String,
        healthDataTypeName: String,
    ): Change

    fun isIntervalData(healthDataName: String): Boolean
}
