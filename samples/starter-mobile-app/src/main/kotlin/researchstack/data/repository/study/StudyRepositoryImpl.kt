package researchstack.data.repository.study

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import researchstack.data.datasource.grpc.GrpcStudyDataSource
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.entity.StudyEntity
import researchstack.data.datasource.local.room.mapper.toDomain
import researchstack.data.datasource.local.room.mapper.toEntity
import researchstack.domain.model.InformedConsent
import researchstack.domain.model.Study
import researchstack.domain.model.StudyStatusModel
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.repository.StudyRepository
import researchstack.domain.repository.UserStatusRepository
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyDao: StudyDao,
    private val grpcStudyDataSource: GrpcStudyDataSource,
    private val userStatusRepository: UserStatusRepository,
) : StudyRepository {

    override fun getStudyById(studyId: String): Flow<Study> =
        studyDao.getStudyById(studyId).map { it.toDomain() }
            .flowOn(Dispatchers.IO)

    override fun getJoinedStudies(): Flow<List<Study>> =
        getStudies { studyDao.getJoinedStudies() }

    override fun getNotJoinedStudies(): Flow<List<Study>> =
        getStudies { studyDao.getNotJoinedStudies() }

    override fun getActiveStudies(): Flow<List<Study>> =
        getStudies { studyDao.getActiveStudies() }

    private fun getStudies(getStudiesFunc: () -> Flow<List<StudyEntity>>): Flow<List<Study>> =
        getStudiesFunc()
            .map { studies -> studies.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)

    override suspend fun insertAll(studies: List<Study>) {
        CoroutineScope(Dispatchers.IO).launch {
            studyDao.insertAll(studies.map { it.toEntity() })
        }
    }

    override suspend fun fetchStudiesFromNetwork() {
        CoroutineScope(Dispatchers.IO).launch {
            grpcStudyDataSource.getStudyList()
                .onSuccess { studies ->
                    studyDao.insertAll(studies.map { it.toEntity() })
                }
                .onFailure {
                    Log.e(TAG, "fail to fetchStudyFromNetwork : ${it.message}")
                }
        }
    }

    override suspend fun fetchJoinedStudiesFromNetwork() {
        CoroutineScope(Dispatchers.IO).launch {
            grpcStudyDataSource.getJoinedStudyList()
                .onSuccess { studies ->
                    studies.forEach { study ->
                        userStatusRepository.getStudyStatusById(study.id).onSuccess {
                            studyDao.save(
                                study.toEntity().copy(joined = true, status = it.number)
                            )
                        }.onFailure {
                            Log.e(TAG, "fail to save study : ${it.message}")
                        }
                    }
                }
                .onFailure {
                    Log.e(TAG, "fail to fetchStudyFromNetwork : ${it.message}")
                }
        }
    }

    override suspend fun getStudyByParticipationCode(participationCode: String): Result<Study> =
        withContext(Dispatchers.IO) {
            val studyEntity = studyDao.getStudyByParticipationCode(participationCode)
            if (studyEntity != null)
                return@withContext Result.success(studyEntity.toDomain())

            grpcStudyDataSource.getStudyByParticipationCode(participationCode)
                .onSuccess { study ->
                    studyDao.insertAll(listOf(study.toEntity()))
                }
        }

    override suspend fun fetchStudyByParticipationCodeFromNetwork(participationCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            grpcStudyDataSource.getStudyByParticipationCode(participationCode)
                .onSuccess { study ->
                    studyDao.insertAll(listOf(study.toEntity()))
                }
                .onFailure {
                    Log.e(
                        TAG,
                        "fail to fetchStudyByParticipationCode : ${it.message}"
                    )
                }
        }
    }

    override suspend fun joinStudy(studyId: String, eligibilityTestResult: EligibilityTestResult): Result<Unit> {
        return withContext(Dispatchers.IO) {
            grpcStudyDataSource.participateInStudy(
                studyId,
                eligibilityTestResult,
                // TODO handle informed consent
                InformedConsent(studyId, "")
            ).onSuccess { subjectNumber ->
                studyDao.updateJoinedAndRegistrationId(
                    studyId,
                    true,
                    subjectNumber,
                    StudyStatusModel.STUDY_STATUS_PARTICIPATING.number
                )
            }.onFailure {
                Log.e("JoinStudy", it.stackTraceToString())
            }.map { }
        }
    }

    override suspend fun withdrawFromStudy(
        studyId: String,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        grpcStudyDataSource.withdrawFromStudy(
            studyId
        )
    }

    companion object {
        private val TAG = StudyRepositoryImpl::class.simpleName
    }
}
