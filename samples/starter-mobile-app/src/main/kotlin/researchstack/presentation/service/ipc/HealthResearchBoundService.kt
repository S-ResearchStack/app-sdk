package researchstack.presentation.service.ipc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import researchstack.domain.usecase.file.SaveFileUseCase
import researchstack.domain.usecase.study.GetStudyByIdUseCase
import javax.inject.Inject

@AndroidEntryPoint
internal class HealthResearchBoundService : Service() {
    @Inject
    lateinit var getStudyUseCase: GetStudyByIdUseCase

    @Inject
    lateinit var saveFileUseCase: SaveFileUseCase

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(p0: Intent): IBinder {
        Log.i(HealthResearchBoundService::class.java.name, "onBind")
        return Messenger(
            MessageIncomingHandler(
                this,
                scope,
                getStudyUseCase,
                saveFileUseCase
            )
        ).binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(HealthResearchBoundService::class.java.name, "onUnbind")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.i(HealthResearchBoundService::class.java.name, "onDestroy")
    }
}
