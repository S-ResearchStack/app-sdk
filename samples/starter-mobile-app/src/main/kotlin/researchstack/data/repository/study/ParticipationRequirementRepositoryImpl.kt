package researchstack.data.repository.study

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.data.datasource.grpc.GrpcStudyDataSource
import researchstack.data.datasource.grpc.mapper.toEntity
import researchstack.data.datasource.local.room.dao.ParticipationRequirementDao
import researchstack.data.datasource.local.room.mapper.toDomain
import researchstack.domain.model.InformedConsent
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.repository.ParticipationRequirementRepository

class ParticipationRequirementRepositoryImpl(
    private val participationRequirementDao: ParticipationRequirementDao,
    private val grpcStudyDataSource: GrpcStudyDataSource,
) : ParticipationRequirementRepository {
    override suspend fun getParticipationRequirement(studyId: String): Result<ParticipationRequirement> =
        withContext(Dispatchers.IO) {
            runCatching {
                val requirement = participationRequirementDao.getParticipationRequirement(studyId)
                if (requirement != null) {
                    return@runCatching requirement.toDomain()
                }

                fetchParticipationRequirementFromNetwork(studyId)

                participationRequirementDao.getParticipationRequirement(studyId)
                    ?.toDomain()
                    ?: throw IllegalStateException("")
            }
        }

    override suspend fun fetchParticipationRequirementFromNetwork(studyId: String) {
        grpcStudyDataSource.getParticipationRequirement(studyId)
            .onSuccess {
                participationRequirementDao.insertAll(listOf(it.toEntity(studyId)))
            }
            .onFailure {
                Log.e(TAG, "fail to fetchParticipationRequirementFromNetwork : ${it.message}")
            }
    }

    override suspend fun saveParticipationRequirementResultToLocal(
        studyId: String,
        eligibilityTestResult: EligibilityTestResult,
        signedInformedConsent: InformedConsent,
    ) = runCatching {
        participationRequirementDao.setResult(
            studyId,
            eligibilityTestResult.surveyResult,
            signedInformedConsent.imageUrl
        )
    }.onFailure {
        Log.e(
            TAG,
            "fail to saveParticipationRequirementResultToLocal : ${it.message}"
        )
    }

    companion object {
        private val TAG = ParticipationRequirementRepositoryImpl::class.simpleName
    }
}
