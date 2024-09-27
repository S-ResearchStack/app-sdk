package researchstack.presentation.viewmodel.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.domain.model.UserProfileModel
import researchstack.domain.usecase.profile.RegisterProfileUseCase
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val registerProfileUseCase: RegisterProfileUseCase,
) : ViewModel() {
    private var _registerResult: MutableLiveData<Result<Unit>?> = MutableLiveData(null)
    val registerResult: LiveData<Result<Unit>?>
        get() = _registerResult

    fun registerUser(userProfileModel: UserProfileModel) {
        CoroutineScope(Dispatchers.IO).launch {
            _registerResult.postValue(registerProfileUseCase(userProfileModel))
            _registerResult.postValue(Result.success(Unit))
        }
    }

    fun resetResult() {
        _registerResult.postValue(null)
    }
}
