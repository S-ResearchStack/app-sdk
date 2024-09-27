package researchstack.domain.model

data class UserProfile(
    var height: Float,
    var weight: Float,
    var yearBirth: Int,
    var gender: Gender,
    var isMetricUnit: Boolean? = null,
)

fun UserProfile?.isValid(): Boolean =
    this != null && yearBirth > 0 && gender != Gender.UNKNOWN && height > 0f && weight > 0f && isMetricUnit != null

enum class Gender {
    FEMALE,
    MALE,
    UNKNOWN,
}
