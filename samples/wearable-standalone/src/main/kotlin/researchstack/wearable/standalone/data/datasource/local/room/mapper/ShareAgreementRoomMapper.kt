package researchstack.wearable.standalone.data.datasource.local.room.mapper

import researchstack.domain.model.priv.PrivDataType
import researchstack.wearable.standalone.data.datasource.local.room.entity.ShareAgreementEntity
import researchstack.wearable.standalone.domain.model.ShareAgreement

fun ShareAgreement.toEntity(): ShareAgreementEntity = ShareAgreementEntity(
    studyId,
    dataType.toString(),
    approval,
)

fun ShareAgreementEntity.toDomain(): ShareAgreement {
    val dataType = listOfNotNull(
        runCatching { PrivDataType.valueOf(dataType) }.getOrNull(),
    ).first()

    return ShareAgreement(
        studyId,
        dataType,
        approval
    )
}
