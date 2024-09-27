package researchstack.domain.usecase.shareagreement

import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.log.AppLog
import researchstack.domain.repository.LogRepository
import researchstack.domain.repository.ShareAgreementRepository
import javax.inject.Inject

class UpdateShareAgreementUseCase @Inject constructor(
    private val shareAgreementRepository: ShareAgreementRepository,
    private val logRepository: LogRepository,
) {
    suspend operator fun invoke(shareAgreement: ShareAgreement): Result<Unit> {
        logRepository.saveAppLog(
            AppLog("Update ShareAgreement").apply { put("message", "$shareAgreement") }
        )
        return shareAgreementRepository.updateShareAgreement(shareAgreement)
    }
}
