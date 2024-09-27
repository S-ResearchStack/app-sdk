package researchstack.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import researchstack.presentation.worker.SyncDataWorker
import javax.inject.Inject

@HiltViewModel
class SyncHealthDataViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    fun syncHealthData() {
        WorkManager
            .getInstance(getApplication<Application>().applicationContext)
            .enqueue(
                OneTimeWorkRequestBuilder<SyncDataWorker>()
                    .build()
            )
    }
}
