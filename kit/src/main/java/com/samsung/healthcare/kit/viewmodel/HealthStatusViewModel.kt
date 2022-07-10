package com.samsung.healthcare.kit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.healthdata.data.DataType
import com.samsung.healthcare.kit.app.getLatestStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthStatusViewModel(
    private val dataType: DataType
) : ViewModel() {

    private val _healthState = MutableStateFlow(HealthState(""))
    val healthState: StateFlow<HealthState> = _healthState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO job can be canceled
            while (true) {
                val latestData = dataType.getLatestStatus()
                latestData?.let {
                    _healthState.value = HealthState(it)
                }
                // TODO how to handle interval
                delay(10000L)
            }
        }
    }

    data class HealthState(val state: Any)
}
