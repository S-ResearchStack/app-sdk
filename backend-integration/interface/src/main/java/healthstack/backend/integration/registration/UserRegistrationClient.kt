package healthstack.backend.integration.registration

/**
 * Interface for registering users who joined through the app to the backend.
 */
interface UserRegistrationClient {

    /**
     * Used to register user's information who joined through the app to the backend.
     *
     * @param idToken An encrypted token containing the user's information issued when the logs in to the application.
     * @param user Data including profile information collected during the Eligibility Check and the basic information of user.
     */
    suspend fun registerUser(idToken: String, user: User)
}
