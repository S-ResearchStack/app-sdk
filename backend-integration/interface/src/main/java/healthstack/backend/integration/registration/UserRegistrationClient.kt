package healthstack.backend.integration.registration

interface UserRegistrationClient {
    suspend fun registerUser(idToken: String, user: User)
}
