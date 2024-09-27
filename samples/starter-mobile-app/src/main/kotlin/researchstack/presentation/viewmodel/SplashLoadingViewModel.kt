package researchstack.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import researchstack.auth.domain.usecase.CheckSignInUseCase
import researchstack.domain.usecase.study.GetJoinedStudiesUseCase
import researchstack.presentation.initiate.route.Route
import javax.inject.Inject

@HiltViewModel
class SplashLoadingViewModel @Inject constructor(
    private val checkSignInUseCase: CheckSignInUseCase,
    private val getJoinedStudiesUseCase: GetJoinedStudiesUseCase,
) : ViewModel() {
    private var _isReady: MutableLiveData<Boolean> = MutableLiveData(false)
    val isReady: LiveData<Boolean>
        get() = _isReady

    private var _routeDestination: MutableLiveData<Route?> = MutableLiveData(null)
    val routeDestination: LiveData<Route?>
        get() = _routeDestination

    var startMainPage = MutableLiveData<Int>(0)
        private set

    fun setStartRouteDestination() {
        viewModelScope.launch {
            val startRoute = if (checkSignInUseCase().getOrDefault(false)) Route.Main
            else Route.Welcome

            _routeDestination.postValue(startRoute)
            _isReady.postValue(true)
        }
    }

    fun setStartMainPage() {
        viewModelScope.launch {
            startMainPage.value = if (getJoinedStudiesUseCase().first().isEmpty()) 0
            else 1
        }
    }
}
