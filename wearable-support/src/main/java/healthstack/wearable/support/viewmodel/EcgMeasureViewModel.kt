package healthstack.wearable.support.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import healthstack.common.room.dao.EcgDao
import healthstack.common.model.EcgSet
import healthstack.common.model.WearDataType
import healthstack.common.MeasureState
import healthstack.wearable.support.data.EcgTracker
import healthstack.wearable.support.data.pref.TrackMeasureTimePref
import healthstack.wearable.support.helper.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import healthstack.wearable.support.data.DataSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EcgMeasureViewModel @Inject constructor(
    application: Application,
    private val ecgTracker: EcgTracker,
    private val trackMeasureTimePref: TrackMeasureTimePref,
    private val ecgDao: EcgDao,
    private val dataSender: DataSender,
) : AndroidViewModel(application) {
    private val _remainPercent = MutableLiveData<Int>()
    val remainPercent: LiveData<Int>
        get() = _remainPercent

    private val _measureState = MutableLiveData<MeasureState>()
    val measureState: LiveData<MeasureState>
        get() = _measureState

    private val ecgSets = mutableListOf<EcgSet>()
    private var startMotionTime = 0L

    private var errorState = ErrorState.Normal

    fun startTrackingEcg() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            errorState = errorState.resetState()
            ecgTracker.startTracking().collect { ecgSet ->
                if (_measureState.value == MeasureState.Completed) return@collect
                if (ecgSet.leadOff == 5) {
                    failureHandler()
                } else {
                    errorState = errorState.resetState()
                    ecgSets.add(ecgSet)

                    val measureState =
                        if (getEcgCount() >= ECG_MEASURE_NUM) MeasureState.Completed
                        else MeasureState.Measuring
                    _remainPercent.postValue(getRemainPercent())
                    _measureState.postValue(measureState)

                    if (measureState == MeasureState.Completed) {
                        stopTracking()
                        val sessionId = Instant.now().toEpochMilli()
                        viewModelScope.launch(Dispatchers.IO) {
                            trackMeasureTimePref.add(sessionId)
                        }
                        ecgSets.forEach { it.sessionId = sessionId }
                        dataSender.sendData(ecgSets, WearDataType.WEAR_ECG).onFailure {
                            ecgDao.insertAll(ecgSets)
                        }
                    }
                }
            }
        }
    }

    private fun failureHandler() {
        _remainPercent.postValue(0)
        ecgSets.clear()
        errorState = errorState.nextState()
        if (errorState.isFirstError()) {
            startMotionTime = System.currentTimeMillis()
            _measureState.postValue(MeasureState.Motion)
        } else if (System.currentTimeMillis() - startMotionTime >= ECG_MOTION_DURATION) {
            _measureState.postValue(MeasureState.MotionOverLoad)
            stopTracking()
        }
    }

    private fun getRemainPercent() =
        if (ecgSets.isEmpty()) 0
        else getEcgCount() * 100 / ECG_MEASURE_NUM

    private fun getEcgCount() =
        ecgSets.first().ecgs.size * ecgSets.size

    fun stopTracking() = runBlocking { ecgTracker.stopTracking() }

    fun pushState(state: MeasureState) {
        _measureState.postValue(state)
    }

    companion object {
        private const val ECG_MEASURE_SECOND: Int = 30
        const val ECG_MOTION_DURATION: Long = 3000
        private const val ECG_HZ = 500
        private const val ECG_MEASURE_NUM = ECG_MEASURE_SECOND * ECG_HZ
    }
}
