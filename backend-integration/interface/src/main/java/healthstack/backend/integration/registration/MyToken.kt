package healthstack.backend.integration.registration

data class MyToken(
    val id: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
)
