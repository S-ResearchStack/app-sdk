package healthstack.app.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import healthstack.app.status.HealthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class HealthStatusViewModel(
    healthStatus: HealthStatus,
    intervalTimeMillis: Long,
) : ViewModel() {
    private val _healthState = MutableStateFlow(HealthState(""))
    val healthState: StateFlow<HealthState> = _healthState
    var lifecycle = Lifecycle.Event.ON_ANY
    private val backgroundLifeCycle = listOf(
        Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_DESTROY
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                if (!backgroundLifeCycle.contains(lifecycle)) {
                    val latestData = healthStatus.getLatestStatus()
                    latestData?.let {
                        _healthState.value = HealthState(it)
                    }
                }
                // TODO how to handle periodical update
                delay(intervalTimeMillis)
            }
        }
    }
}
