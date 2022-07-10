package com.samsung.healthcare.kit.external.network

import com.samsung.healthcare.kit.external.data.User

interface UserRegistrationClient {
    suspend fun registerUser(idToken: String, user: User)
}
