package researchstack.auth.data.repository.auth.supertokens

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.auth.data.datasource.auth.supertokens.SuperTokensAuthRequester
import researchstack.auth.domain.model.Authentication
import researchstack.auth.domain.model.BasicAuthentication
import researchstack.auth.domain.model.IdToken
import researchstack.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SuperTokensAuthRepository @Inject constructor(
    private val superTokensAuthRequester: SuperTokensAuthRequester,
) : AuthRepository {
    override fun getAuthType() = AuthRepository.AuthType.ID_PASSWORD

    override fun getProvider(): String = "super-tokens"

    override suspend fun signIn(auth: Authentication): Result<IdToken> = withContext(Dispatchers.IO) {
        runCatching {
            auth as BasicAuthentication
        }.mapCatching {
            superTokensAuthRequester.signIn(it.id, it.password)
                .getOrThrow()
        }.mapCatching {
            it.accessToken?.let { IdToken(it, getProvider()) } ?: throw IllegalStateException("no id token")
        }
    }
}
