package healthstack.healthdata.link

import java.time.Instant

/**
 * An interface for handling health data.
 *
 * It has several methods for general health data client.
 */
interface HealthDataLink {
    /**
     * A method checking if all permissions are acquired.
     */
    suspend fun hasAllPermissions(): Boolean

    /**
     * A method requesting all permissions.
     */
    suspend fun requestPermissions()

    /**
     * A method reading health data.
     *
     * It reads health data generated between startTime and endTime.
     *
     * @param startTime Start point of time range
     * @param endTime End point of time range
     * @param healthDataTypeName Type of health data to read.
     * @return [HealthData]
     */
    suspend fun getHealthData(
        startTime: Instant,
        endTime: Instant,
        healthDataTypeName: String,
    ): HealthData

    /**
     * A method requesting initial changes token.
     *
     * It requests changes token for a specific (time) moment.
     *
     * @param healthDataTypeName Type of health data to read.
     * @return Changes token.
     */
    suspend fun getChangesToken(healthDataTypeName: String): String

    /**
     * A method reading all events about changes generated after given changes token.
     *
     * Change events are usually categorized of Upsertion and Deletion.
     *
     * @param token Changes token.
     * @param healthDataTypeName Type of health data to read.
     * @return [Change]
     */
    suspend fun getChanges(
        token: String,
        healthDataTypeName: String,
    ): Change

    /**
     * A method checking if the given health data type is Interval data.
     *
     * @param healthDataTypeName Type of health data to read.
     */
    fun isIntervalData(healthDataName: String): Boolean
}
