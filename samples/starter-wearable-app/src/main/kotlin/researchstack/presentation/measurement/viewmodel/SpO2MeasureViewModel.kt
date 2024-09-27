package researchstack.presentation.measurement.viewmodel

import android.animation.ValueAnimator
import android.app.Application
import android.os.CountDownTimer
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
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.priv.SpO2
import researchstack.domain.usecase.TrackDataUseCase
import researchstack.domain.usecase.TrackMeasureTimeUseCase
import researchstack.presentation.main.screen.HomeScreenItem
import researchstack.presentation.main.screen.getItemPrefKey
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SpO2MeasureViewModel @Inject constructor(
    application: Application,
    private val trackMeasureTimeUseCase: TrackMeasureTimeUseCase,
    private val trackDataUseCase: TrackDataUseCase,
) : AndroidViewModel(application) {

    private val _measureState = MutableLiveData<SpO2MeasureState>()
    val measureState: LiveData<SpO2MeasureState>
        get() = _measureState

    private val _progress = MutableLiveData(0f)
    val progress: MutableLiveData<Float>
        get() = _progress

    private val _spO2Value = MutableLiveData(0)
    val spO2Value: LiveData<Int?>
        get() = _spO2Value

    private var disconnectedCount = 0
    private var startMotionTime = 0L
    private var circularProgress = 0f
    private var measureTime = 0f
    private val countDownTimer: CountDownTimer =
        object : CountDownTimer(SPO2_MEASURE_SECOND, MEASUREMENT_TICK) {
            override fun onTick(timeLeft: Long) {
                measureTime++
                circularProgress = measureTime / 60
                _progress.postValue(circularProgress)
            }

            override fun onFinish() {
                if (_measureState.value != SpO2MeasureState.Completed) onMeasurementFailed()
            }
        }

    init {
        startTracking()
    }

    private fun onMeasurementFailed() {
        stopTracking()
        _measureState.postValue(SpO2MeasureState.Failed)
    }

    private suspend fun onMeasurementCompleted(spo2: SpO2) {
        val sessionId = Instant.now().toEpochMilli()
        viewModelScope.launch(Dispatchers.IO) {
            trackMeasureTimeUseCase(HomeScreenItem.BLOOD_OXYGEN.getItemPrefKey(), sessionId)
        }
        spo2.sessionId = sessionId
        stopTracking()
        viewModelScope.launch(Dispatchers.Main) {
            _progress.value?.let { it ->
                ValueAnimator.ofFloat(it, 1f).apply {
                    duration = 400
                    addUpdateListener {
                        _progress.postValue(it.animatedValue as Float)
                    }
                }.start()
            }
        }
        delay(500)
        _spO2Value.postValue(spo2.spO2)
        _measureState.postValue(SpO2MeasureState.Completed)
        trackDataUseCase.sendData(spo2, PrivDataType.WEAR_SPO2)
    }

    private fun onCalculating() {
        _measureState.postValue(SpO2MeasureState.Measuring)
        disconnectedCount = 0
    }

    private fun onMotionDetected() {
        _measureState.postValue(SpO2MeasureState.Motion)
        disconnectedCount += 1
        if (disconnectedCount == 1) {
            startMotionTime = System.currentTimeMillis()
        }
        if (System.currentTimeMillis() - startMotionTime >= SPO2_MOTION_DURATION) {
            Log.d(SpO2MeasureViewModel::class.simpleName, "Motion time out > 3s")
            processGuideAgain()
        }
    }

    private fun processGuideAgain() {
        stopTracking()
        _measureState.postValue(SpO2MeasureState.GuideAgain)
    }

    fun reMeasuring() {
        _progress.value = 0f
        _measureState.value = SpO2MeasureState.Measuring
        startTracking()
    }

    fun startTracking() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            measureTime = 0f
            disconnectedCount = 0
            _progress.postValue(0f)
            countDownTimer.start()
            Log.i(SpO2MeasureViewModel::class.simpleName, "start tracking for SpO2")
            trackDataUseCase.startTracking(PrivDataType.WEAR_SPO2).collect { it ->
                val spO2 = it as SpO2
                Log.d(SpO2MeasureViewModel::class.simpleName, "spO2.status = ${spO2.status}")
                when (spO2.status) {
                    SpO2.Flag.LOW_SIGNAL -> processGuideAgain()
                    SpO2.Flag.DEVICE_MOVING -> onMotionDetected()
                    SpO2.Flag.INITIAL_STATUS -> _measureState.postValue(SpO2MeasureState.Initial)
                    SpO2.Flag.CALCULATING -> onCalculating()
                    SpO2.Flag.MEASUREMENT_COMPLETED -> onMeasurementCompleted(spO2)
                    SpO2.Flag.FAILED -> onMeasurementFailed()
                }
            }
        }
    }

    fun stopTracking() = runBlocking {
        countDownTimer.cancel()
        trackDataUseCase.stopTracking(PrivDataType.WEAR_SPO2)
    }

    companion object {
        const val SPO2_MOTION_DURATION = 3000L
        const val SPO2_MEASURE_SECOND = 30000L
        const val MEASUREMENT_TICK = 500L
    }

    enum class SpO2MeasureState {
        Measuring,
        Motion,
        Completed,
        GuideAgain,
        Failed,
        Initial,
    }
}
