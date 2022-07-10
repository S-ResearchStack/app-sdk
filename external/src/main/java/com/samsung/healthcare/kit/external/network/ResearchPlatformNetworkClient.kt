package com.samsung.healthcare.kit.external.network

import com.samsung.healthcare.kit.external.data.HealthData
import com.samsung.healthcare.kit.external.data.User
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ResearchPlatformNetworkClient {
    @POST("/api/projects/{projectId}/health-data")
    suspend fun sync(
        @Header("id-token") idToken: String,
        @Path("projectId") projectId: String,
        @Body healthData: HealthData,
    )

    @POST("/api/projects/{projectId}/users")
    suspend fun registerUser(
        @Header("id-token") idToken: String,
        @Path("projectId") projectId: String,
        @Body user: User,
    )
}
