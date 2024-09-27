package researchstack.wearable.standalone.domain.repository

interface StudyRepository {
    suspend fun joinStudy(studyId: String): Result<Unit>
}
