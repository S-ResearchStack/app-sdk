package researchstack.presentation.measurement.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.usecase.TrackDataUseCase
import researchstack.domain.usecase.TrackMeasureTimeUseCase
import researchstack.presentation.main.screen.HomeScreenItem
import researchstack.presentation.main.screen.getItemPrefKey
import researchstack.presentation.measurement.viewmodel.helper.ErrorState
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EcgMeasureViewModel @Inject constructor(
    application: Application,
    private val trackMeasureTimeUseCase: TrackMeasureTimeUseCase,
    private val trackDataUseCase: TrackDataUseCase,
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
            Log.i(EcgMeasureViewModel::class.simpleName, "start tracking for ECG")
            errorState = errorState.resetState()
            trackDataUseCase.startTracking(PrivDataType.WEAR_ECG).collect { it ->
                val ecgSet = it as EcgSet
                Log.i(EcgMeasureViewModel::class.simpleName, "ecgSet.leadOff ${ecgSet.leadOff}")
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
                            trackMeasureTimeUseCase(HomeScreenItem.ECG.getItemPrefKey(), sessionId)
                        }
                        ecgSets.forEach { it.sessionId = sessionId }
                        trackDataUseCase.sendData(ecgSets, PrivDataType.WEAR_ECG)
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

    fun stopTracking() = runBlocking { trackDataUseCase.stopTracking(PrivDataType.WEAR_ECG) }

    fun pushState(state: MeasureState) {
        _measureState.postValue(state)
    }

    companion object {
        const val ECG_MEASURE_SECOND: Int = 30
        const val ECG_MOTION_DURATION: Long = 3000
        private const val ECG_HZ = 500
        private const val ECG_MEASURE_NUM = ECG_MEASURE_SECOND * ECG_HZ
    }

    enum class MeasureState {
        Initial,
        Measuring,
        Motion,
        MotionOverLoad,
        Completed,
        Failed,
    }
}
