package researchstack.presentation.viewmodel.study

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InsightPasswordViewModel @Inject constructor() : ViewModel() {
    var isAuthentic = MutableLiveData<Boolean>()
        private set
    fun setAuthenticState(state: Boolean) {
        isAuthentic.value = state
    }
}
