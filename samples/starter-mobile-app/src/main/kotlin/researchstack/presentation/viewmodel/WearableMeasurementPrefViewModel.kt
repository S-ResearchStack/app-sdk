package researchstack.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.domain.usecase.wearable.SetEcgMeasurementEnabledUseCase
import javax.inject.Inject

@HiltViewModel
class WearableMeasurementPrefViewModel @Inject constructor(
    private val setEcgMeasurementEnabledUseCase: SetEcgMeasurementEnabledUseCase
) : ViewModel() {
    fun setEcgMeasurementEnabled(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            setEcgMeasurementEnabledUseCase(enable)
        }
    }
}
