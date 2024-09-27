package researchstack.presentation.viewmodel.study

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.Study
import researchstack.domain.model.eligibilitytest.EligibilityTestResult

// FIXME naming and how to handle
class SharedStudyJoinViewModel : ViewModel() {

    private val _study = MutableStateFlow<Study?>(null)
    val study: StateFlow<Study?> = _study

    fun setStudy(study: Study) {
        _study.value = study
    }

    private val _participationRequirement = MutableStateFlow<ParticipationRequirement?>(null)
    val participationRequirement: StateFlow<ParticipationRequirement?> = _participationRequirement

    fun setParticipationRequirement(requirement: ParticipationRequirement) {
        _participationRequirement.value = requirement
    }

    lateinit var eligibilityTestResult: EligibilityTestResult
}
