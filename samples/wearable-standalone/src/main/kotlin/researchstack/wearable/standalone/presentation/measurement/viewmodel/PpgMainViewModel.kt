package researchstack.wearable.standalone.presentation.measurement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.wearable.standalone.domain.usecase.TrackMeasureTimeUseCase
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem
import researchstack.wearable.standalone.presentation.main.screen.getItemPrefKey
import researchstack.wearable.standalone.presentation.main.viewmodel.getTimeString
import javax.inject.Inject

@HiltViewModel
class PpgMainViewModel @Inject constructor(
    application: Application,
    private val trackMeasureTimeUseCase: TrackMeasureTimeUseCase
) : AndroidViewModel(application) {
    val lastMeasurementTime = MutableLiveData<String>()

    fun getLastTimeMeasure(ppgType: String) {
        val key = if (ppgType == HomeScreenItem.PPG_IR.name) {
            HomeScreenItem.PPG_IR.getItemPrefKey()
        } else HomeScreenItem.PPG_RED.getItemPrefKey()
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                viewModelScope.launch {
                    trackMeasureTimeUseCase(key).collect {
                        lastMeasurementTime.postValue(it.getTimeString())
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}
