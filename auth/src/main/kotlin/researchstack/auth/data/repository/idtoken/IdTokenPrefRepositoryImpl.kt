package researchstack.auth.data.repository.idtoken

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.auth.data.datasource.local.pref.IdTokenPref
import researchstack.auth.domain.model.IdToken
import researchstack.auth.domain.repository.IdTokenRepository

class IdTokenPrefRepositoryImpl(
    private val idTokenPref: IdTokenPref
) : IdTokenRepository {
    override suspend fun getIdToken(): IdToken? = withContext(Dispatchers.IO) {
        idTokenPref.getIdToken()
    }

    override suspend fun updateIdToken(idToken: IdToken) = withContext(Dispatchers.IO) {
        idTokenPref.updateIdToken(idToken)
    }

    override suspend fun clearIdToken() = withContext(Dispatchers.IO) {
        idTokenPref.clearIdToken()
    }
}
