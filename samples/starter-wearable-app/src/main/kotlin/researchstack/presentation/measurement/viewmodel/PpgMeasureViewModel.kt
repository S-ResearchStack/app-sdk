package researchstack.presentation.measurement.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.usecase.TrackDataUseCase
import researchstack.domain.usecase.TrackMeasureTimeUseCase
import researchstack.presentation.main.screen.HomeScreenItem
import researchstack.presentation.main.screen.getItemPrefKey
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class PpgMeasureViewModel
    @Inject
    constructor(
        private val trackMeasureTimeUseCase: TrackMeasureTimeUseCase,
        private val trackDataUseCase: TrackDataUseCase,
    ) : ViewModel() {
        private var sendDataScope = CoroutineScope(Dispatchers.IO)
        private val _measureState = MutableStateFlow(MeasureState.None)
        val measureState: StateFlow<MeasureState> = _measureState

        private val ppgRedList = mutableListOf<PpgRed>()
        private val ppgIrList = mutableListOf<PpgIr>()
        private var ppgType = HomeScreenItem.PPG_RED.name
        private val _timer = MutableLiveData(0)
        val timer: LiveData<Int>
            get() = _timer

        private val countDownTimer =
            object : CountDownTimer(
                TIME_LIMIT_TO_STOP_MEASURING_IN_SECOND * 1000,
                1000,
            ) {
                override fun onTick(p0: Long) {
                    _timer.value?.let {
                        _timer.postValue(it + 1)
                    }
                }

                override fun onFinish() {
                    viewModelScope.launch(Dispatchers.IO) {
                        stopTrackingByPpgType()
                        sendPpgDataToMobile()
                        updateState(MeasureState.Completed)
                    }
                }
            }

        fun measurePpgData(ppgType: String) {
            if (_measureState.value != MeasureState.Measuring) {
                updateState(MeasureState.Measuring)
                countDownTimer.start()
                this.ppgType = ppgType
                if (ppgType == HomeScreenItem.PPG_IR.name) {
                    getPpgIrData()
                } else {
                    getPpgRedData()
                }
            }
        }

        private fun getPpgIrData() {
            viewModelScope.launch(Dispatchers.IO) {
                trackDataUseCase.startTracking(PrivDataType.WEAR_PPG_IR).collect { ppg ->
                    if (_measureState.value == MeasureState.Completed) return@collect
                    ppgIrList.add(ppg as PpgIr)
                    if (ppgIrList.size >= TIME_LIMIT_FOR_SENDING_DATA_IN_SECOND * PPG_HZ) {
                        val copyPpgIrList = ArrayList<PpgIr>()
                        copyPpgIrList.addAll(ppgIrList)
                        ppgIrList.clear()
                        sendDataScope.launch(Dispatchers.IO) {
                            trackDataUseCase.sendData(copyPpgIrList, PrivDataType.WEAR_PPG_IR)
                        }
                    }
                }
            }
        }

        private fun getPpgRedData() {
            viewModelScope.launch(Dispatchers.IO) {
                trackDataUseCase.startTracking(PrivDataType.WEAR_PPG_RED).collect { ppg ->
                    if (_measureState.value == MeasureState.Completed) return@collect
                    ppgRedList.add(ppg as PpgRed)
                    if (ppgRedList.size >= TIME_LIMIT_FOR_SENDING_DATA_IN_SECOND * PPG_HZ) {
                        val copyPpgRedList = ArrayList<PpgRed>()
                        copyPpgRedList.addAll(ppgRedList)
                        ppgRedList.clear()
                        sendDataScope.launch(Dispatchers.IO) {
                            trackDataUseCase.sendData(copyPpgRedList, PrivDataType.WEAR_PPG_RED)
                        }
                    }
                }
            }
        }

        fun stopTracking() {
            runBlocking {
                countDownTimer.cancel()
                stopTrackingByPpgType()
            }
        }

        private suspend fun stopTrackingByPpgType() {
            val sessionId = Instant.now().toEpochMilli()
            if (ppgType == HomeScreenItem.PPG_IR.name) {
                trackDataUseCase.stopTracking(PrivDataType.WEAR_PPG_IR)
                trackMeasureTimeUseCase(HomeScreenItem.PPG_IR.getItemPrefKey(), sessionId)
            } else {
                trackDataUseCase.stopTracking(PrivDataType.WEAR_PPG_RED)
                trackMeasureTimeUseCase(HomeScreenItem.PPG_RED.getItemPrefKey(), sessionId)
            }
        }

        fun sendPpgDataToMobile() {
            viewModelScope.launch(Dispatchers.IO) {
                if (ppgType == HomeScreenItem.PPG_IR.name) {
                    trackDataUseCase.sendData(ppgIrList, PrivDataType.WEAR_PPG_IR)
                } else {
                    trackDataUseCase.sendData(ppgRedList, PrivDataType.WEAR_PPG_RED)
                }
            }
        }

        fun updateState(measureState: MeasureState) {
            _measureState.value = measureState
        }

        override fun onCleared() {
            stopTracking()
            super.onCleared()
        }

        enum class MeasureState {
            None,
            Measuring,
            Completed,
        }

        companion object {
            const val TIME_LIMIT_FOR_SENDING_DATA_IN_SECOND = 300
            const val PPG_HZ = 100
            const val TIME_LIMIT_TO_STOP_MEASURING_IN_SECOND = 21600L
        }
    }
