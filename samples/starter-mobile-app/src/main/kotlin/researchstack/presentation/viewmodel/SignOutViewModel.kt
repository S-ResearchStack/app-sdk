package researchstack.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.auth.domain.usecase.ClearAccountUseCase
import researchstack.auth.domain.usecase.ClearIdTokenUseCase
import researchstack.domain.usecase.auth.ClearLocalDBUseCase
import researchstack.presentation.service.TrackerDataForegroundService
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    application: Application,
    private val clearIdTokenUseCase: ClearIdTokenUseCase,
    private val clearAccountUseCase: ClearAccountUseCase,
    private val clearLocalDBUseCase: ClearLocalDBUseCase
) : AndroidViewModel(application) {
    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            clearIdTokenUseCase()
            clearAccountUseCase()
            clearLocalDBUseCase()
            stopAllServicesAndWorkers()
        }
    }
    private fun stopAllServicesAndWorkers() {
        val context: Context = getApplication<Application>().applicationContext
        WorkManager.getInstance(context).cancelAllWork()
        context.stopService(Intent(context, TrackerDataForegroundService::class.java))
    }
}
