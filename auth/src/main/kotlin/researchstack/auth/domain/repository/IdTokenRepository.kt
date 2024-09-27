package researchstack.auth.domain.repository

import researchstack.auth.domain.model.IdToken

interface IdTokenRepository {
    suspend fun getIdToken(): IdToken?

    suspend fun updateIdToken(idToken: IdToken)

    suspend fun clearIdToken()
}
