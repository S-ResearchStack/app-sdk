package researchstack.domain.repository

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.Study
import researchstack.domain.model.eligibilitytest.EligibilityTestResult

interface StudyRepository {
    fun getNotJoinedStudies(): Flow<List<Study>>

    fun getJoinedStudies(): Flow<List<Study>>

    fun getActiveStudies(): Flow<List<Study>>

    fun getStudyById(studyId: String): Flow<Study>

    suspend fun getStudyByParticipationCode(participationCode: String): Result<Study>

    suspend fun insertAll(studies: List<Study>)

    // FIXME naming
    suspend fun fetchStudiesFromNetwork()

    suspend fun fetchJoinedStudiesFromNetwork()

    suspend fun fetchStudyByParticipationCodeFromNetwork(participationCode: String)

    suspend fun joinStudy(studyId: String, eligibilityTestResult: EligibilityTestResult): Result<Unit>

    suspend fun withdrawFromStudy(studyId: String): Result<Unit>
}
