package researchstack.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.usecase.TrackDataUseCase
import javax.inject.Inject

@AndroidEntryPoint
class AlarmToDeleteFile : BroadcastReceiver() {
    @Inject
    lateinit var trackDataUseCase: TrackDataUseCase

    override fun onReceive(
        context: Context,
        intent: Intent?,
    ) {
        Log.i(TAG, "onReceive: delete file")
        CoroutineScope(Dispatchers.IO).launch {
            PrivDataType.values().forEach {
                trackDataUseCase.deleteFile(it)
            }
        }
    }

    companion object {
        val TAG = AlarmToDeleteFile::class.simpleName
    }
}
