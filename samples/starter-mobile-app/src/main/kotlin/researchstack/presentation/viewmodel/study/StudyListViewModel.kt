package researchstack.presentation.viewmodel.study

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.domain.model.Study
import researchstack.domain.model.StudyStatusModel.STUDY_STATUS_PARTICIPATING
import researchstack.domain.usecase.study.FetchStudyStatusUseCase
import researchstack.domain.usecase.study.GetJoinedStudiesUseCase
import researchstack.domain.usecase.study.LookupNotJoinedStudiesUseCase
import researchstack.domain.usecase.task.RemoveStudyTasksUseCase
import javax.inject.Inject

@HiltViewModel
class StudyListViewModel @Inject constructor(
    application: Application,
    private val lookupNotJoinedStudiesUseCase: LookupNotJoinedStudiesUseCase,
    private val getJoinedStudiesUseCase: GetJoinedStudiesUseCase,
    private val fetchStudyStatusUseCase: FetchStudyStatusUseCase,
    private val removeStudyTasksUseCase: RemoveStudyTasksUseCase,
    private val studyShardViewModel: SharedStudyJoinViewModel,
) : AndroidViewModel(application) {
    private val _studies = MutableStateFlow<List<Study>>(emptyList())
    val studies: StateFlow<List<Study>> = _studies

    private val _myActiveStudies = MutableStateFlow<List<Study>>(emptyList())
    val myActiveStudies: StateFlow<List<Study>> = _myActiveStudies

    private val _myCompletedStudies = MutableStateFlow<List<Study>>(emptyList())
    val myCompletedStudies: StateFlow<List<Study>> = _myCompletedStudies

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun lookupStudy() {
        viewModelScope.launch(Dispatchers.IO) {
            lookupNotJoinedStudiesUseCase().collect {
                _studies.value = it
            }
        }
    }

    fun getMyStudy() {
        fetchStudyStatus()
        viewModelScope.launch(Dispatchers.IO) {
            getJoinedStudiesUseCase().collect {
                categorizeStudyByStatus(it)
            }
        }
    }

    private fun categorizeStudyByStatus(listStudy: List<Study>) {
        val listActive = mutableListOf<Study>()
        val listCompleted = mutableListOf<Study>()
        viewModelScope.launch(Dispatchers.IO) {
            listStudy.forEach { study ->
                if (study.status == null || study.status == STUDY_STATUS_PARTICIPATING) {
                    listActive.add(study)
                } else {
                    listCompleted.add(study)
                }
            }
            _myCompletedStudies.value = listCompleted.toList()
            _myActiveStudies.value = listActive.toList()
        }
    }

    fun setStudy(study: Study) {
        studyShardViewModel.setStudy(study)
    }

    private fun fetchStudyStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                fetchStudyStatusUseCase()
            }.onSuccess {
                Log.i(StudyListViewModel::class.simpleName, "success to update current status")
                it.forEach { study ->
                    Log.i(StudyListViewModel::class.simpleName, "send br for ${study.id}")
                    getApplication<Application>().applicationContext.sendBroadcast(
                        Intent("com.samsung.research.health.STUDY_STATUS_UPDATED")
                            .putExtra("studyId", study.id)
                            .putExtra("status", study.status?.name)
                            .putExtra("subjectId", study.registrationId)
                    )
                }
                it.forEach { study ->
                    if (study.status != STUDY_STATUS_PARTICIPATING) {
                        removeStudyTasksUseCase(study.id)
                    }
                }
            }.onFailure {
                Log.i(StudyListViewModel::class.simpleName, "fail to update current status")
            }
        }
    }
}
