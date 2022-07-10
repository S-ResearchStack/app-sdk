package com.samsung.healthcare.kit.external.network

import android.content.Context
import com.samsung.healthcare.kit.external.R
import com.samsung.healthcare.kit.external.background.HealthDataSyncClient
import com.samsung.healthcare.kit.external.data.HealthData
import com.samsung.healthcare.kit.external.data.User
import com.samsung.healthcare.kit.external.network.util.RetrofitFactory

class ResearchPlatformAdapter private constructor(
    private val networkClient: ResearchPlatformNetworkClient,
    private val projectId: String,
) : HealthDataSyncClient, UserRegistrationClient {

    override suspend fun sync(idToken: String, healthData: HealthData) {
        networkClient.sync(idToken, projectId, healthData)
    }

    override suspend fun registerUser(idToken: String, user: User) {
        networkClient.registerUser(idToken, projectId, user)
    }

    companion object {
        private lateinit var INSTANCE: ResearchPlatformAdapter

        fun initialize(context: Context) {
            synchronized(this) {
                if (::INSTANCE.isInitialized.not()) {
                    INSTANCE = ResearchPlatformAdapter(
                        RetrofitFactory.create(
                            context.getString(R.string.research_platform_address),
                            ResearchPlatformNetworkClient::class.java
                        ),
                        context.getString(R.string.project_Id)
                    )
                }
            }
        }

        fun getInstance(): ResearchPlatformAdapter = INSTANCE
    }
}
