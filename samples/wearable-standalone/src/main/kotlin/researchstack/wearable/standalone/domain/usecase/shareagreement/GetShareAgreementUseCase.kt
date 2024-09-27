package researchstack.wearable.standalone.domain.usecase.shareagreement

import kotlinx.coroutines.flow.Flow
import researchstack.wearable.standalone.domain.model.ShareAgreement
import researchstack.wearable.standalone.domain.repository.ShareAgreementRepository
import javax.inject.Inject

class GetShareAgreementUseCase @Inject constructor(
    private val shareAgreementRepository: ShareAgreementRepository,
) {
    operator fun invoke(studyId: String): Flow<List<ShareAgreement>> =
        shareAgreementRepository.getShareAgreement(studyId)
}
