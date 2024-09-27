package researchstack.wearable.standalone.domain.model

import researchstack.domain.model.priv.PrivDataType

data class ShareAgreement(
    val studyId: String,
    val dataType: Enum<*>,
    val approval: Boolean = false,
) {
    init {
        val allowedEnums = listOf(
            PrivDataType::class,
        )

        if (allowedEnums.any { it.isInstance(dataType) }.not())
            throw IllegalArgumentException("Only type in ${allowedEnums.map { it.simpleName }} can be unifiedDataType")
    }
}
