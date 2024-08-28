package healthstack.backend.integration.registration

data class SignInResponse(
    val id: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
)
