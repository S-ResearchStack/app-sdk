package researchstack.data.datasource.grpc.mapper

import researchstack.domain.model.Study
import researchstack.backend.grpc.Study as GrpcStudy

fun GrpcStudy.toDomain(): Study = Study(
    id = this.id,
    participationCode = participationCode,
    name = this.studyInfo.name,
    description = this.studyInfo.description,
    logoUrl = this.studyInfo.logoUrl,
    organization = this.studyInfo.organization,
    duration = this.studyInfo.duration,
    period = this.studyInfo.period,
    requirements = this.studyInfo.requirementsList
)
