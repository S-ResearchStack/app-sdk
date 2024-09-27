package researchstack.domain.repository

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.shealth.SHealthDataType

interface ShareAgreementRepository {
    suspend fun saveShareAgreement(shareAgreement: ShareAgreement): Result<Unit>

    suspend fun saveShareAgreements(shareAgreements: List<ShareAgreement>): Result<Unit>

    suspend fun removeAll(studyId: String): Result<Unit>

    fun getAgreedHealthDataTypes(studyId: String): Flow<List<SHealthDataType>>

    fun getAgreedWearableDataTypes(studyId: String): Flow<List<PrivDataType>>

    fun getShareAgreement(studyId: String): Flow<List<ShareAgreement>>

    fun getApprovalShareAgreementWithStudyAndDataType(studyId: String, dataType: String): Boolean

    suspend fun updateShareAgreement(shareAgreement: ShareAgreement): Result<Unit>

    fun getShareAgreementFromDataType(dataType: String): Flow<List<ShareAgreement>>
}
