package com.samsung.healthcare.kit.external.background

import com.samsung.healthcare.kit.external.data.HealthData

interface HealthDataSyncClient {
    suspend fun sync(idToken: String, healthData: HealthData)
}
