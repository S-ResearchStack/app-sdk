package researchstack.auth.data.datasource.auth.cognito

import android.util.Log
import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import aws.sdk.kotlin.services.cognitoidentityprovider.model.AuthFlowType
import aws.sdk.kotlin.services.cognitoidentityprovider.model.AuthenticationResultType
import aws.sdk.kotlin.services.cognitoidentityprovider.model.InitiateAuthRequest
import javax.inject.Inject

class CognitoAuthRequester @Inject constructor(
    private val cognitoClient: CognitoIdentityProviderClient,
    private val cognitoClientId: String,
) {
    suspend fun signIn(
        username: String,
        password: String,
    ): Result<AuthenticationResultType> =
        runCatching {
            val authParams = mutableMapOf<String, String>()
            authParams[AUTH_PARAM_USERNAME] = username
            authParams[AUTH_PARAM_PASSWORD] = password

            val request = InitiateAuthRequest {
                clientId = cognitoClientId
                authParameters = authParams
                authFlow = AuthFlowType.UserPasswordAuth
            }

            cognitoClient.initiateAuth(request)
        }.onFailure {
            Log.e(CognitoAuthRequester::class.simpleName, it.stackTraceToString())
        }.onSuccess {
            Log.i(
                CognitoAuthRequester::class.simpleName,
                "User session is created. Status is ${it.authenticationResult}"
            )
        }.map { response ->
            response.authenticationResult?.let {
                return Result.success(it)
            }
            return Result.failure(Exception(AUTH_RESULT_NULL))
        }

    companion object {
        private const val AUTH_PARAM_USERNAME = "USERNAME"
        private const val AUTH_PARAM_PASSWORD = "PASSWORD"

        private const val AUTH_RESULT_NULL = "AuthenticationResult is null"
    }
}
