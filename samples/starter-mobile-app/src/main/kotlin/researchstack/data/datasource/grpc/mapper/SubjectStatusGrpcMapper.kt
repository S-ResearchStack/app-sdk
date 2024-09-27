package researchstack.data.datasource.grpc.mapper

import researchstack.backend.grpc.SubjectStatus
import researchstack.domain.model.StudyStatusModel

fun SubjectStatus.toDomain(): StudyStatusModel = when (this) {
    SubjectStatus.SUBJECT_STATUS_WITHDRAWN -> StudyStatusModel.STUDY_STATUS_WITHDRAW
    SubjectStatus.SUBJECT_STATUS_PARTICIPATING -> StudyStatusModel.STUDY_STATUS_PARTICIPATING
    SubjectStatus.SUBJECT_STATUS_COMPLETED -> StudyStatusModel.STUDY_STATUS_COMPLETE
    SubjectStatus.SUBJECT_STATUS_DROP -> StudyStatusModel.STUDY_STATUS_DROP
    else -> StudyStatusModel.STUDY_STATUS_UNSPECIFIED
}
