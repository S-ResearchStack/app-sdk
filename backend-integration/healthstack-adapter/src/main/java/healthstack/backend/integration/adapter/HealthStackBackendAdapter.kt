package healthstack.backend.integration.adapter

import android.net.Uri
import android.webkit.MimeTypeMap
import android.webkit.MimeTypeMap.getFileExtensionFromUrl
import healthstack.backend.integration.BackendFacade
import healthstack.backend.integration.exception.RegisterException
import healthstack.backend.integration.exception.UserAlreadyExistsException
import healthstack.backend.integration.registration.SignInResponse
import healthstack.backend.integration.registration.SignUpResponse
import healthstack.backend.integration.registration.SignInRequest
import healthstack.backend.integration.registration.SignUpRequest
import healthstack.backend.integration.registration.UserProfile
import healthstack.healthdata.link.HealthData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.time.LocalDateTime

class HealthStackBackendAdapter(
    private val networkClient: HealthStackBackendAPI,
    private val projectId: String,
) : BackendFacade {
    init {
        require(projectId.isNotBlank())
    }

    private val okHttpClient = OkHttpClient()

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

    override suspend fun signUp(email: String, password: String): SignUpResponse =
        try {
            networkClient.signUp(SignUpRequest(email, password))
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                HttpURLConnection.HTTP_CONFLICT -> throw UserAlreadyExistsException()
                else -> throw RegisterException()
            }
        }

    override suspend fun signIn(email: String, password: String): SignInResponse =
        networkClient.signIn(SignInRequest(email, password))

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

    override suspend fun uploadTaskResultAsFile(idToken: String, sourcePath: String, targetPath: String) {
        val signedUrl = networkClient.getUploadUrl(idToken, projectId, targetPath)
        val uploadFile = File(sourcePath)
        val extension = getFileExtensionFromUrl(Uri.fromFile(uploadFile).toString())
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

        val request = Request.Builder()
            .url(signedUrl)
            .put(uploadFile.asRequestBody(mime?.toMediaTypeOrNull()))
            .build()

        okHttpClient.newCall(request).execute().use { res ->
            if (!res.isSuccessful) throw IOException("Unexpected code $res")
        }
    }

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
