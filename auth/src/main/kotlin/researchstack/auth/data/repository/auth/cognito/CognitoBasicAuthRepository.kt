package researchstack.auth.data.repository.auth.cognito

import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.auth.data.datasource.auth.cognito.CognitoAuthRequester
import researchstack.auth.domain.model.Authentication
import researchstack.auth.domain.model.BasicAuthentication
import researchstack.auth.domain.model.IdToken
import researchstack.auth.domain.repository.AuthRepository
import javax.inject.Inject

class CognitoBasicAuthRepository @Inject constructor(
    clientRegion: String,
    clientId: String,
    private val cognitoAuthRequester: CognitoAuthRequester =
        CognitoAuthRequester(
            CognitoIdentityProviderClient { region = clientRegion },
            clientId,
        )
) : AuthRepository {
    override fun getAuthType() = AuthRepository.AuthType.ID_PASSWORD

    override fun getProvider(): String = "cognito"

    override suspend fun signIn(auth: Authentication): Result<IdToken> = withContext(Dispatchers.IO) {
        runCatching {
            auth as BasicAuthentication
        }.mapCatching {
            cognitoAuthRequester.signIn(it.id, it.password)
                .getOrThrow()
        }.mapCatching {
            it.idToken?.let { IdToken(it, getProvider()) } ?: throw IllegalStateException("no id token")
        }
    }
}
