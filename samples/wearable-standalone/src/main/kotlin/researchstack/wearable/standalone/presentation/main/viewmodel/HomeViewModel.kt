package researchstack.wearable.standalone.presentation.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import researchstack.data.local.pref.WearableMeasurementPref
import researchstack.wearable.standalone.data.local.pref.dataStore
import researchstack.wearable.standalone.domain.usecase.TrackMeasureTimeUseCase
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem
import researchstack.wearable.standalone.presentation.main.screen.getItemPrefKey
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

private val TAG = "HomeViewModel"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    trackMeasureTimeUseCase: TrackMeasureTimeUseCase
) : AndroidViewModel(application) {
    private val wearableMeasurementPreference = WearableMeasurementPref(application.dataStore)

    private val _lastEcgMeasurementTime = MutableLiveData<String>()
    val lastEcgMeasurementTime: LiveData<String>
        get() = _lastEcgMeasurementTime

    private val _lastSpo2MeasurementTime = MutableLiveData<String>()
    val lastSpo2MeasurementTime: LiveData<String>
        get() = _lastSpo2MeasurementTime

    private val _lastBloodPressureMeasurementTime = MutableLiveData<String>()
    val lastBloodPressureMeasurementTime: LiveData<String>
        get() = _lastBloodPressureMeasurementTime

    private val _lastBiaMeasurementTime = MutableLiveData<String>()
    val lastBiaMeasurementTime: LiveData<String>
        get() = _lastBiaMeasurementTime

    private val _lastPpgRedMeasurementTime = MutableLiveData<String>()
    val lastPpgRedMeasurementTime: LiveData<String>
        get() = _lastPpgRedMeasurementTime

    private val _lastPpgIrMeasurementTime = MutableLiveData<String>()
    val lastPpgIrMeasurementTime: LiveData<String>
        get() = _lastPpgIrMeasurementTime

    private val _ecgMeasurementEnabled = MutableLiveData(true)
    val ecgMeasurementEnabled: LiveData<Boolean>
        get() = _ecgMeasurementEnabled

    init {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                for (item in HomeScreenItem.values()) {
                    Log.d(TAG, "lastMeasure: ${item.name}")
                    viewModelScope.launch {
                        trackMeasureTimeUseCase(item.getItemPrefKey()).onEach {
                            Log.d(TAG, "lastMeasure income ${item.name} $it")
                        }.flatMapLatest {
                            flowOf(it.getTimeString())
                        }.collect {
                            getMutableLiveDataByType(item).postValue(it)
                        }
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            wearableMeasurementPreference.ecgMeasurementEnabled
                .collect {
                    _ecgMeasurementEnabled.postValue(it)
                }
        }
    }

    private fun getMutableLiveDataByType(item: HomeScreenItem): MutableLiveData<String> {
        return when (item) {
            HomeScreenItem.BLOOD_OXYGEN -> _lastSpo2MeasurementTime
            HomeScreenItem.ECG -> _lastEcgMeasurementTime
            HomeScreenItem.BODY_COMPOSITION -> _lastBiaMeasurementTime
            HomeScreenItem.PPG_RED -> _lastPpgRedMeasurementTime
            HomeScreenItem.PPG_IR -> _lastPpgIrMeasurementTime
        }
    }

    fun getLiveDataByType(item: HomeScreenItem): LiveData<String> {
        return when (item) {
            HomeScreenItem.BLOOD_OXYGEN -> lastSpo2MeasurementTime
            HomeScreenItem.ECG -> lastEcgMeasurementTime
            HomeScreenItem.BODY_COMPOSITION -> lastBiaMeasurementTime
            HomeScreenItem.PPG_RED -> lastPpgRedMeasurementTime
            HomeScreenItem.PPG_IR -> lastPpgIrMeasurementTime
        }
    }

    fun isEcgEnabled() = ecgMeasurementEnabled
}

fun Long.getTimeString(): String {
    val format_date = SimpleDateFormat("dd/MM/yyyy")
    val format_time = SimpleDateFormat("hh:mm a")
    val currentDate = Date()
    val currentDateString = format_date.format(currentDate)
    return if (this == 0L) {
        ""
    } else {
        val lastMeasure = Date(this)
        val lastMeasureDate = format_date.format(lastMeasure)
        if (currentDateString == lastMeasureDate) {
            format_time.format(lastMeasure)
        } else {
            lastMeasureDate
        }
    }
}
