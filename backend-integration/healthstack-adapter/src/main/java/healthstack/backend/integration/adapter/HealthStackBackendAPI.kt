package healthstack.backend.integration.adapter

import healthstack.backend.integration.registration.User
import healthstack.backend.integration.registration.UserProfile
import healthstack.backend.integration.task.TaskResult
import healthstack.backend.integration.task.TaskSpec
import healthstack.healthdata.link.HealthData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface HealthStackBackendAPI {
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

    @PATCH("/api/projects/{projectId}/users/{userId}")
    suspend fun updateUser(
        @Header("id-token") idToken: String,
        @Path("projectId") projectId: String,
        @Path("userId") userId: String,
        @Body userProfile: UserProfile,
    )

    @GET("/api/projects/{projectId}/tasks")
    suspend fun getTasks(
        @Header("id-token") idToken: String,
        @Path("projectId") projectId: String,
        @Query("last_sync_time") lastSyncTime: LocalDateTime,
        @Query("end_time") endTime: LocalDateTime,
        @Query("status") status: String = "PUBLISHED",
    ): List<TaskSpec>

    @PATCH("/api/projects/{projectId}/tasks")
    suspend fun uploadTaskResult(
        @Header("id-token") idToken: String,
        @Path("projectId") projectId: String,
        @Body taskResult: List<TaskResult>,
    )
}
