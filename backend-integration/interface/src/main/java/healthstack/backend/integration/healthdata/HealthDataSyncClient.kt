package healthstack.backend.integration.healthdata

import healthstack.healthdata.link.HealthData

interface HealthDataSyncClient {
    suspend fun sync(idToken: String, healthData: HealthData)
}
