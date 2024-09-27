package researchstack.auth.domain.model

sealed class Authentication

object OidcAuthentication : Authentication()

data class BasicAuthentication(val id: String, val password: String) : Authentication()
