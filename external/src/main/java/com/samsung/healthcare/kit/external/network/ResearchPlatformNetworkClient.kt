package com.samsung.healthcare.kit.external.network

import com.samsung.healthcare.kit.external.data.HealthData
import com.samsung.healthcare.kit.external.data.TaskResult
import com.samsung.healthcare.kit.external.data.TaskSpec
import com.samsung.healthcare.kit.external.data.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

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

    @GET("api/projects/{projectId}/tasks")
    suspend fun getTasks(
        @Header("id-token") idToken: String,
        @Path("projectId") projectId: String,
        @Query("last_sync_time") lastSyncTime: LocalDateTime,
        @Query("end_time") endTime: LocalDateTime = LocalDateTime.now(),
        @Query("status") status: String = "PUBLISHED",
    ): List<TaskSpec>

    @PATCH("api/projects/{projectId}/tasks")
    suspend fun uploadTaskResult(
        @Header("id-token") idToken: String,
        @Path("projectId") projectId: String,
        @Body taskResult: List<TaskResult>,
    )
}
