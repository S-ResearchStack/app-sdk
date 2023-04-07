package healthstack.backend.integration.adapter

import healthstack.backend.integration.BackendFacade
import healthstack.backend.integration.exception.RegisterException
import healthstack.backend.integration.exception.UserAlreadyExistsException
import healthstack.backend.integration.registration.UserProfile
import healthstack.healthdata.link.HealthData
import java.net.HttpURLConnection
import java.time.LocalDateTime

class HealthStackBackendAdapter(
    private val networkClient: HealthStackBackendAPI,
    private val projectId: String,
) : BackendFacade {
    init {
        require(projectId.isNotBlank())
    }

    override suspend fun sync(idToken: String, healthData: HealthData) {
        networkClient.sync(idToken, projectId, healthData.instantToString())
    }

    override suspend fun registerUser(idToken: String, user: healthstack.backend.integration.registration.User) {
        try {
            networkClient.registerUser(idToken, projectId, user)
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                HttpURLConnection.HTTP_CONFLICT -> throw UserAlreadyExistsException()
                else -> throw RegisterException()
            }
        }
    }

    override suspend fun updateUser(idToken: String, userId: String, userProfile: UserProfile) =
        networkClient.updateUser(idToken, projectId, userId, userProfile)

    @Throws(IllegalArgumentException::class)
    override suspend fun getTasks(
        idToken: String,
        lastSyncTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<healthstack.backend.integration.task.TaskSpec> {
        require(endTime.isAfter(lastSyncTime))

        return networkClient.getTasks(idToken, projectId, lastSyncTime, endTime)
    }

    override suspend fun uploadTaskResult(idToken: String, result: healthstack.backend.integration.task.TaskResult) =
        networkClient.uploadTaskResult(idToken, projectId, listOf(result))

    companion object {
        private lateinit var INSTANCE: HealthStackBackendAdapter

        fun initialize(platformEndpoint: String, researchProjectId: String) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = HealthStackBackendAdapter(
                        RetrofitFactory.create(
                            platformEndpoint,
                            HealthStackBackendAPI::class.java
                        ),
                        researchProjectId
                    )
                }
            }
        }

        fun getInstance(): HealthStackBackendAdapter = INSTANCE
    }
}
