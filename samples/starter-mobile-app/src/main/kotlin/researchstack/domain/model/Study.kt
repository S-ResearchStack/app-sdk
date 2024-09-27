package researchstack.domain.model

data class Study(
    val id: String,
    val participationCode: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val organization: String,
    val duration: String,
    val period: String,
    val requirements: List<String>,
    val joined: Boolean = false,
    val status: StudyStatusModel? = null,
    val registrationId: String? = null,
)
