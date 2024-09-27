package researchstack.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import researchstack.domain.usecase.profile.DeregisterProfileUseCase
import researchstack.domain.usecase.study.WithdrawFromAllStudyUseCase
import javax.inject.Inject

@HiltViewModel
class DeregisterViewModel @Inject constructor(
    application: Application,
    private val withdrawFromAllStudyUseCase: WithdrawFromAllStudyUseCase,
    private val deregisterProfileUseCase: DeregisterProfileUseCase
) : AndroidViewModel(application) {
    fun deregister(
        onComplete: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // prevent deleting IdToken before deregister is done
            runBlocking {
                withdrawFromAllStudyUseCase()
                deregisterProfileUseCase()
            }

            onComplete()
        }
    }
}
