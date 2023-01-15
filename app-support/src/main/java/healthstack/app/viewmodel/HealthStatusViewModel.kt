package healthstack.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import healthstack.app.status.StatusDataType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthStatusViewModel(
    private val dataType: StatusDataType
) : ViewModel() {

    private val _healthState = MutableStateFlow(HealthState(""))
    val healthState: StateFlow<HealthState> = _healthState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val latestData = dataType.getLatestStatus()
                latestData?.let {
                    _healthState.value = HealthState(it)
                }
                // TODO how to handle periodical update
                delay(10000L)
            }
        }
    }

    data class HealthState(val state: Any)
}
