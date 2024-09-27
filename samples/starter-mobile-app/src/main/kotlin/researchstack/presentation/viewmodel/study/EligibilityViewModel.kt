package researchstack.presentation.viewmodel.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.model.eligibilitytest.answer.ChoiceAnswer
import researchstack.domain.model.eligibilitytest.answer.TextAnswer
import researchstack.domain.usecase.study.GetParticipationRequirementUseCase
import javax.inject.Inject

@HiltViewModel
class EligibilityViewModel @Inject constructor(
    private val getParticipationRequirementOfStudy: GetParticipationRequirementUseCase,
    private val studySharedViewModel: SharedStudyJoinViewModel,
) : ViewModel() {

    val participationRequirement: StateFlow<ParticipationRequirement?> = studySharedViewModel.participationRequirement

    fun getParticipationRequirement() {
        viewModelScope.launch {
            studySharedViewModel.study.value?.let {
                getParticipationRequirementOfStudy(it.id).onSuccess { requirement ->
                    studySharedViewModel.setParticipationRequirement(requirement)
                }.onFailure {
                    // TODO handle error
                }
            }
        }
    }

    // TODO create usecase for eligibility
    fun checkEligibility(userResponse: MutableMap<String, String>): Boolean =
        participationRequirement.value?.eligibilityTest?.answers?.all { answer ->
            when (answer) {
                is TextAnswer -> {
                    answer.answers.contains(userResponse[answer.questionId])
                }

                is ChoiceAnswer -> answer.options.map { it.value }
                    .contains(userResponse[answer.questionId])

                else -> true
            }
        } ?: false

    fun setEligibilityTestResult(eligibilityTestResult: EligibilityTestResult) {
        studySharedViewModel.eligibilityTestResult = eligibilityTestResult
    }
}
