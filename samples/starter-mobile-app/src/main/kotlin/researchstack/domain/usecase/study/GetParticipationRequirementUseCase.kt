package researchstack.domain.usecase.study

import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.repository.ParticipationRequirementRepository
import javax.inject.Inject

class GetParticipationRequirementUseCase
@Inject constructor(private val repository: ParticipationRequirementRepository) {
    suspend operator fun invoke(studyId: String): Result<ParticipationRequirement> =
        repository.getParticipationRequirement(studyId)
}
