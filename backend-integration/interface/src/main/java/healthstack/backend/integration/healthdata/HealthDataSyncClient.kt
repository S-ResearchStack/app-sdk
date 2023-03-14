package healthstack.backend.integration.healthdata

import healthstack.healthdata.link.HealthData

/**
 * Interface for syncing data with backend.
 */
interface HealthDataSyncClient {
    /**
     * Used to transmit the user's health data to the backend. Backend can distinguish users through id token.
     *
     * @param idToken An encrypted token containing the user's information issued when the logs in to the application.
     * @param healthData Health data of users collected through the [healthstack.healthdata.link.HealthDataLink].
     */
    suspend fun sync(idToken: String, healthData: HealthData)
}
