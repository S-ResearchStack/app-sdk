package researchstack.presentation.viewmodel.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.domain.exception.EmptyStudyCodeException
import researchstack.domain.model.Study
import researchstack.domain.usecase.study.GetCloseStudyUseCase
import javax.inject.Inject

@HiltViewModel
class StudyCodeViewModel @Inject constructor(
    private val getCloseStudyUseCase: GetCloseStudyUseCase,
    private val studyShardViewModel: SharedStudyJoinViewModel,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(Init)
    val state: StateFlow<State> = _state

    fun getClosedStudy(studyCode: String) {
        viewModelScope.launch {
            _state.value = Loading

            getCloseStudyUseCase(studyCode)
                .onSuccess {
                    _state.value = Success(it)
                }.onFailure {
                    _state.value = Fail(it)
                }
        }
    }

    fun setStudy(study: Study) {
        studyShardViewModel.setStudy(study)
    }

    fun setStateToInit() {
        _state.value = Init
    }

    fun handleEmptyStudyCode() {
        _state.value = Fail(EmptyStudyCodeException)
    }

    sealed class State

    object Init : State()

    object Loading : State()

    class Success(val study: Study) : State()

    class Fail(val error: Throwable) : State()
}
