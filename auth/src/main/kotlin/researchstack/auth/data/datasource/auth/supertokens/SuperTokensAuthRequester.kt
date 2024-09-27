package researchstack.auth.data.datasource.auth.supertokens

import researchstack.backend.grpc.AuthServiceGrpcKt
import researchstack.backend.grpc.SignInRequest
import researchstack.backend.grpc.SignInResponse
import researchstack.backend.grpc.SignUpRequest
import researchstack.backend.grpc.SignUpResponse

class SuperTokensAuthRequester(
    private val authServiceCoroutineStub: AuthServiceGrpcKt.AuthServiceCoroutineStub
) {
    suspend fun signIn(
        email: String,
        password: String,
    ): Result<SignInResponse> =
        runCatching {
            superTokenSignUp(email, password)
            superTokenSignIn(email, password).getOrThrow()
        }

    private suspend fun superTokenSignUp(
        email: String,
        password: String
    ): Result<SignUpResponse> =
        runCatching {
            authServiceCoroutineStub.signUp(
                SignUpRequest.newBuilder().setEmail(email).setPassword(password).build()
            )
        }

    private suspend fun superTokenSignIn(
        email: String,
        password: String
    ): Result<SignInResponse> =
        runCatching {
            authServiceCoroutineStub.signIn(
                SignInRequest.newBuilder().setEmail(email).setPassword(password).build()
            )
        }
}
