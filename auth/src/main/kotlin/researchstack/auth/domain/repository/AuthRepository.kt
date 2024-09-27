package researchstack.auth.domain.repository

import researchstack.auth.domain.model.Authentication
import researchstack.auth.domain.model.IdToken

interface AuthRepository {
    fun getAuthType(): AuthType

    fun getProvider(): String

    suspend fun signIn(auth: Authentication): Result<IdToken>

    enum class AuthType {
        OIDC,
        ID_PASSWORD,
    }
}
