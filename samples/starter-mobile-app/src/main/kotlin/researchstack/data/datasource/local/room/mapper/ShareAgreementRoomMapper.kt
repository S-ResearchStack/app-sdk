package researchstack.data.datasource.local.room.mapper

import researchstack.data.datasource.local.room.entity.ShareAgreementEntity
import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType

fun ShareAgreement.toEntity(): ShareAgreementEntity = ShareAgreementEntity(
    studyId,
    dataType.toString(),
    approval,
)

fun ShareAgreementEntity.toDomain(): ShareAgreement {
    val dataType = listOfNotNull(
        runCatching { SHealthDataType.valueOf(dataType) }.getOrNull(),
        runCatching { TrackerDataType.valueOf(dataType) }.getOrNull(),
        runCatching { PrivDataType.valueOf(dataType) }.getOrNull(),
        runCatching { DeviceStatDataType.valueOf(dataType) }.getOrNull(),
    ).first()

    return ShareAgreement(
        studyId,
        dataType,
        approval
    )
}
