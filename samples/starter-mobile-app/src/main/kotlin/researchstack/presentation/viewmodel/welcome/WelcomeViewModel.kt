package researchstack.presentation.viewmodel.welcome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.R
import researchstack.auth.domain.model.Authentication
import researchstack.auth.domain.model.OidcAuthentication
import researchstack.auth.domain.repository.AuthRepository
import researchstack.auth.domain.usecase.CheckSignInUseCase
import researchstack.auth.domain.usecase.ClearAccountUseCase
import researchstack.auth.domain.usecase.GetAccountUseCase
import researchstack.auth.domain.usecase.SignInUseCase
import researchstack.domain.exception.AlreadyExistedUserException
import researchstack.domain.model.UserProfileModel
import researchstack.domain.usecase.ReLoginUseCase
import researchstack.domain.usecase.profile.RegisterProfileUseCase
import researchstack.presentation.worker.FetchStudyWorker
import researchstack.presentation.worker.WorkerRegistrar.registerAllPeriodicWorkers
import java.time.LocalDate
import javax.inject.Inject

private val anonymousUser = UserProfileModel(
    firstName = "",
    lastName = "",
    birthday = LocalDate.now(),
    email = "",
    phoneNumber = "",
    address = ""
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
    private val signInUseCase: SignInUseCase,
    private val registerProfileUseCase: RegisterProfileUseCase,
    private val reLoginUseCase: ReLoginUseCase,
    private val clearAccountUseCase: ClearAccountUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val checkSignInUseCase: CheckSignInUseCase,
) : AndroidViewModel(application) {
    sealed class RegisterState
    object None : RegisterState()
    object Registering : RegisterState()
    object Success : RegisterState()
    class Fail(val message: String) : RegisterState()

    val authType = authRepository.getAuthType()

    val authProvider = authRepository.getProvider()

    private val _registerState = MutableStateFlow<RegisterState>(None)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _hasAccount = MutableStateFlow<Boolean?>(null)
    val hasAccount: StateFlow<Boolean?> = _hasAccount

    private fun handleSamsungAccountFailure(ex: Throwable) {
        if (ex == AlreadyExistedUserException) {
            // NOTE the re-login process is not supported yet
            // should get joiend study and subject id and should set data permission
            _registerState.value = Success
            return
        }
        _registerState.value = Fail(getApplication<Application>().getString(R.string.require_samsung_account_login))
    }

    private suspend fun handleFailure(ex: Throwable) {
        Log.e(WelcomeViewModel::class.simpleName, ex.stackTraceToString())
        Log.e(WelcomeViewModel::class.simpleName, "fail to register profile")
        clearAccountUseCase()
        if (ex == AlreadyExistedUserException) {
            _registerState.value = Fail(getApplication<Application>().getString(R.string.already_registered_user))
            return
        }
        _registerState.value = Fail(getApplication<Application>().getString(R.string.fail_to_signin))
    }

    fun registerUser(auth: Authentication) {
        _registerState.value = Registering
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase(auth)
                .mapCatching {
                    registerProfileUseCase(anonymousUser).getOrThrow()
                }.onSuccess {
                    _registerState.value = Success
                    fetchStudy()
                    registerAllPeriodicWorkers(getApplication<Application>().applicationContext)
                }.onFailure { ex ->
                    if (auth is OidcAuthentication) {
                        handleSamsungAccountFailure(ex)
                    } else {
                        handleFailure(ex)
                    }
                }
        }
    }

    private fun fetchStudy() {
        WorkManager
            .getInstance(getApplication<Application>().applicationContext)
            .enqueue(
                OneTimeWorkRequestBuilder<FetchStudyWorker>()
                    .build()
            )
    }

    fun checkAccountExists() {
        viewModelScope.launch(Dispatchers.IO) {
            _hasAccount.value = getAccountUseCase().isSuccess
        }
    }

    fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkSignInUseCase().getOrNull() == true) {
                _registerState.value = Success
            } else {
                _registerState.value = Fail("Fail to login: maybe network issue")
            }
        }
    }
}
