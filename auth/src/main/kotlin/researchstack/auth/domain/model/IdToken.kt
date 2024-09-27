package researchstack.auth.domain.model

import com.auth0.jwt.JWT
import java.time.LocalDateTime
import java.time.ZoneId

data class IdToken(val token: String, val issuer: String) {
    private val decodedJWT by lazy {
        JWT().decodeJwt(token)
    }

    val subject: String = decodedJWT.subject

    fun isExpired(): Boolean {
        val nowPlusOneMinute =
            LocalDateTime.now().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant()
        val expiredAt = decodedJWT.expiresAtAsInstant

        return nowPlusOneMinute >= expiredAt
    }

    fun isSameUser(other: IdToken) = this.subject == other.subject
}
