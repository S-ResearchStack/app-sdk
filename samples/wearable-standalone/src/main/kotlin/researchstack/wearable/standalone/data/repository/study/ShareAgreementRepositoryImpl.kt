package researchstack.wearable.standalone.data.repository.study

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.wearable.standalone.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.wearable.standalone.data.datasource.local.room.mapper.toDomain
import researchstack.wearable.standalone.data.datasource.local.room.mapper.toEntity
import researchstack.wearable.standalone.domain.model.ShareAgreement
import researchstack.wearable.standalone.domain.repository.ShareAgreementRepository
import javax.inject.Inject

class ShareAgreementRepositoryImpl @Inject constructor(
    private val shareAgreementDao: ShareAgreementDao,
) : ShareAgreementRepository {
    override suspend fun saveShareAgreement(shareAgreement: ShareAgreement): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                shareAgreementDao.insert(shareAgreement.toEntity())
            }.onFailure { Log.e(TAG, "Failed to saveShareAgreement : ${it.message}") }
        }

    override suspend fun saveShareAgreements(shareAgreements: List<ShareAgreement>): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                shareAgreementDao.insert(shareAgreements.map { it.toEntity() })
            }.onFailure { Log.e(TAG, "Failed to saveShareAgreements : ${it.message}") }
        }

    override suspend fun removeAll(studyId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                shareAgreementDao.deleteAll(studyId)
            }.onFailure { Log.e(TAG, "Failed to removeAll shareAgreements : ${it.message}") }
        }

    override fun getAgreedHealthDataTypes(studyId: String) =
        shareAgreementDao.getAgreedShareAgreement(studyId).map { entities ->
            entities.map { it.toDomain().dataType }.filterIsInstance<SHealthDataType>()
        }

    override fun getAgreedWearableDataTypes(studyId: String): Flow<List<PrivDataType>> =
        shareAgreementDao.getAgreedShareAgreement(studyId).map { entities ->
            entities.map { it.toDomain().dataType }.filterIsInstance<PrivDataType>()
        }

    override fun getShareAgreement(studyId: String): Flow<List<ShareAgreement>> =
        shareAgreementDao.getShareAgreements(studyId)
            .distinctUntilChanged()
            .map { entities ->
                entities.map { it.toDomain() }
            }.flowOn(Dispatchers.IO)

    override fun getApprovalShareAgreementWithStudyAndDataType(studyId: String, dataType: String): Boolean =
        shareAgreementDao.getApprovalShareAgreementWithStudyAndDataType(studyId, dataType)

    override suspend fun updateShareAgreement(shareAgreement: ShareAgreement): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                shareAgreementDao.updateApproval(
                    shareAgreement.studyId,
                    shareAgreement.dataType.toString(),
                    shareAgreement.approval
                )
            }.onFailure { Log.e(TAG, "Update shareAgreements : ${it.message}") }
        }

    override fun getShareAgreementFromDataType(dataType: String): Flow<List<ShareAgreement>> =
        shareAgreementDao.getAgreedShareAgreementFromDataType(dataType)
            .distinctUntilChanged()
            .map { etities -> etities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)

    companion object {
        private val TAG = ShareAgreementRepositoryImpl::class.simpleName
    }
}
