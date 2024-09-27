package researchstack.domain.repository

import researchstack.domain.model.Study
import researchstack.domain.model.StudyStatusModel

interface UserStatusRepository {
    suspend fun getStudyStatusById(studyId: String): Result<StudyStatusModel>
    suspend fun fetchStudyStatusFromNetWork(): List<Study>
}
