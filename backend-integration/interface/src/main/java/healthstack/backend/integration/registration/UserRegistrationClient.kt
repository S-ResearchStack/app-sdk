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

    /**
     * Used to update user's profile who joined through the app to the backend.
     *
     * @param idToken An encrypted token containing the user's information issued when the logs in to the application.
     * @param userId Id of the user to be updated.
     * @param userProfile Data including profile information collected from the Profile View.
     */
    suspend fun updateUser(idToken: String, userId: String, userProfile: UserProfile)
}
