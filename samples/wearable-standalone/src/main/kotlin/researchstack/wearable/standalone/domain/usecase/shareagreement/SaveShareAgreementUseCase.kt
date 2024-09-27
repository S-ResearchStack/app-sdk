package researchstack.wearable.standalone.domain.usecase.shareagreement

import researchstack.wearable.standalone.domain.model.ShareAgreement
import researchstack.wearable.standalone.domain.repository.ShareAgreementRepository
import javax.inject.Inject

class SaveShareAgreementUseCase @Inject constructor(private val shareAgreementRepository: ShareAgreementRepository) {
    suspend fun save(shareAgreements: List<ShareAgreement>): Result<Unit> {
        return shareAgreementRepository.saveShareAgreements(shareAgreements)
    }

    fun getAgreedHealthDataTypes(studyId: String) = shareAgreementRepository.getAgreedHealthDataTypes(studyId)
}
