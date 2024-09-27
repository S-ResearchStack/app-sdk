package researchstack.domain.usecase.shareagreement

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.ShareAgreement
import researchstack.domain.repository.ShareAgreementRepository
import javax.inject.Inject

class GetShareAgreementUseCase @Inject constructor(
    private val shareAgreementRepository: ShareAgreementRepository,
) {
    operator fun invoke(studyId: String): Flow<List<ShareAgreement>> =
        shareAgreementRepository.getShareAgreement(studyId)
}
