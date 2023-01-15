package healthstack.backend.integration.registration

data class User(
    val userId: String,
    val profile: Map<String, Any> = emptyMap(),
)
