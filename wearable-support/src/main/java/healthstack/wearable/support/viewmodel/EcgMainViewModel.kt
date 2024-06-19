package healthstack.wearable.support.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import healthstack.wearable.support.data.pref.TrackMeasureTimePref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EcgMainViewModel @Inject constructor(
    application: Application,
    trackMeasureTimePref: TrackMeasureTimePref,
) : AndroidViewModel(application) {
    private val _lastMeasurementTime = MutableLiveData<String>()
    val lastMeasurementTime: LiveData<String>
        get() = _lastMeasurementTime

    init {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                viewModelScope.launch {
                    trackMeasureTimePref.getLastMeasureFlow().collect {
                        _lastMeasurementTime.postValue(it.getTimeString())
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
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
