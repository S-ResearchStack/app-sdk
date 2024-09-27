package researchstack.wearable.standalone.domain.model

import java.time.LocalDate

data class UserProfileModel(
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val email: String,
    val phoneNumber: String,
    val address: String,
)
