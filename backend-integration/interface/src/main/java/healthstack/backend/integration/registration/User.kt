package healthstack.backend.integration.registration

/**
 * Data including profile information collected during the Eligibility Check and the basic information of user.
 *
 * @property userId ID of user
 * @property profile Information collected during the Eligibility Check.
 */
data class User(
    val userId: String,
    val profile: Map<String, Any> = emptyMap(),
)
