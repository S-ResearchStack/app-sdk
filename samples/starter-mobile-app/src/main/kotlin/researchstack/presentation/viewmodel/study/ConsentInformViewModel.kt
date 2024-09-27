package researchstack.presentation.viewmodel.study

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import researchstack.domain.model.ParticipationRequirement
import javax.inject.Inject

@HiltViewModel
class ConsentInformViewModel @Inject constructor(
    private val studySharedViewModel: SharedStudyJoinViewModel,
) : ViewModel() {

    val participationRequirement: StateFlow<ParticipationRequirement?> = studySharedViewModel.participationRequirement
}
