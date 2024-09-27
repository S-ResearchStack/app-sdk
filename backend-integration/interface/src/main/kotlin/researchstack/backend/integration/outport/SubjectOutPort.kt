package researchstack.backend.integration.outport

import researchstack.backend.grpc.SubjectProfile
import researchstack.backend.grpc.SubjectStatus

interface SubjectOutPort {
    suspend fun registerSubject(subjectProfile: SubjectProfile): Result<Unit>
    suspend fun getSubjectProfile(): Result<SubjectProfile>
    suspend fun updateSubjectProfile(subjectProfile: SubjectProfile): Result<Unit>
    suspend fun deregisterSubject(): Result<Unit>
    suspend fun getSubjectStatus(studyId: String): Result<SubjectStatus>
}
