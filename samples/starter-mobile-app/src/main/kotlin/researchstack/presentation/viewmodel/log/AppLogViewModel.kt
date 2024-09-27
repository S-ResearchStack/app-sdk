package researchstack.presentation.viewmodel.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.domain.model.log.AppLog
import researchstack.domain.repository.LogRepository
import javax.inject.Inject

@HiltViewModel
class AppLogViewModel @Inject constructor(
    application: Application,
    private val logRepository: LogRepository,
) : AndroidViewModel(application) {

    private val _appLogs = MutableStateFlow<List<AppLog>>(emptyList())
    val appLogs: StateFlow<List<AppLog>> = _appLogs

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getLatestLogs()
        }
    }

    private suspend fun getLatestLogs() {
        _appLogs.value = logRepository.getLatestLogs("DataSync", 4 * 24 * 7)
    }

    fun refresh() {
        viewModelScope.launch {
            getLatestLogs()
        }
    }
}
