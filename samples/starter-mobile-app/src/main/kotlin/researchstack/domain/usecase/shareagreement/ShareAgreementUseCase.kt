package researchstack.domain.usecase.shareagreement

import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.log.AppLog
import researchstack.domain.repository.ShareAgreementRepository
import researchstack.domain.usecase.log.AppLogger
import javax.inject.Inject

class ShareAgreementUseCase @Inject constructor(private val shareAgreementRepository: ShareAgreementRepository) {
    suspend fun save(shareAgreements: List<ShareAgreement>): Result<Unit> {
        AppLogger.saveLog(
            AppLog("Save ShareAgreements").apply { put("message", "$shareAgreements") }
        )
        return shareAgreementRepository.saveShareAgreements(shareAgreements)
    }

    fun getAgreedHealthDataTypes(studyId: String) = shareAgreementRepository.getAgreedHealthDataTypes(studyId)
}
