package researchstack.data.datasource.local.room.mapper

import researchstack.data.datasource.local.room.entity.StudyEntity
import researchstack.domain.model.Study
import researchstack.domain.model.StudyStatusModel

fun Study.toEntity(): StudyEntity =
    StudyEntity(
        id = id,
        participationCode = participationCode,
        name = name,
        description = description,
        logoUrl = logoUrl,
        organization = organization,
        duration = duration,
        period = period,
        requirements = requirements,
        joined = joined,
        status = status?.number,
        registrationId = registrationId,
    )

fun StudyEntity.toDomain(): Study =
    Study(
        id = id,
        participationCode = participationCode,
        name = name,
        description = description,
        logoUrl = logoUrl,
        organization = organization,
        duration = duration,
        period = period,
        requirements = requirements,
        joined = joined,
        status = status?.let { StudyStatusModel.fromNumber(it) },
        registrationId = registrationId,
    )
