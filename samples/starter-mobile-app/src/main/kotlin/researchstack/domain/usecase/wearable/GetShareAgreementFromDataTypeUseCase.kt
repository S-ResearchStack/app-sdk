package researchstack.domain.usecase.wearable

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.ShareAgreement
import researchstack.domain.repository.ShareAgreementRepository
import javax.inject.Inject

class GetShareAgreementFromDataTypeUseCase @Inject constructor(
    private val shareAgreementRepository: ShareAgreementRepository,
) {
    operator fun invoke(dataType: String): Flow<List<ShareAgreement>> =
        shareAgreementRepository.getShareAgreementFromDataType(dataType)
}
