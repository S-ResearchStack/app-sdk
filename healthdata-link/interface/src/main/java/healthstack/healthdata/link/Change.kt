package healthstack.healthdata.link

/**
 * A data object representing Change.
 *
 * @property healthData [HealthData]
 * @property token renewed changes token.
 */
class Change(
    val healthData: HealthData,
    val token: String,
)
