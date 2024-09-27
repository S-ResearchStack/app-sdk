package researchstack.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.BuildConfig
import researchstack.data.datasource.local.pref.dataStore
import researchstack.data.local.pref.WearableMeasurementPref
import researchstack.presentation.pref.UIPreference
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class UISettingViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val inClinicModeDurationSec = 60 * 60 * 6L

    private val pagePreference = UIPreference(application.dataStore)

    private val measurementPreference = WearableMeasurementPref(application.dataStore)

    private val _logPageEnabled = MutableStateFlow(false)
    val logPageEnabled: StateFlow<Boolean> = _logPageEnabled

    private val _mobileDataSyncEnable = MutableStateFlow(BuildConfig.ENABLE_CELLULAR_DATA_TO_SYNC_DATA)
    val mobileDataSyncEnable: StateFlow<Boolean> = _mobileDataSyncEnable

    private val _inClinicModeUntil = MutableStateFlow(0L)
    val inClinicModeUntil: StateFlow<Long> = _inClinicModeUntil

    private val _ecgMeasurementEnabled = MutableStateFlow(true)
    val ecgMeasurementEnabled: StateFlow<Boolean> = _ecgMeasurementEnabled

    init {
        viewModelScope.launch(Dispatchers.IO) {
            pagePreference.logPageConfigFlow
                .collect {
                    _logPageEnabled.value = it
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            pagePreference.mobileDataSyncEnabled
                .collect {
                    _mobileDataSyncEnable.value = it
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            pagePreference.inClinicModeUntil
                .collect {
                    _inClinicModeUntil.value = it
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            measurementPreference.ecgMeasurementEnabled
                .collect {
                    _ecgMeasurementEnabled.value = it
                }
        }
    }

    fun isInClinicMode(): Boolean {
        return inClinicModeUntil.value > Instant.now().epochSecond
    }

    fun updateInClinicModeUntil(isInClinicMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isInClinicMode) {
                pagePreference.setInClinicModeUntil(0)
            } else {
                pagePreference.setInClinicModeUntil(Instant.now().plusSeconds(inClinicModeDurationSec).epochSecond)
            }
        }
    }

    fun setMobileDataEnable(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            pagePreference.setMobileDataSync(enable)
        }
    }

    fun setEcgMeasurementEnabled(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementPreference.setEcgMeasurementEnabled(enable)
        }
    }
}
