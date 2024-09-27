package researchstack.presentation.worker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import researchstack.data.datasource.local.pref.dataStore
import researchstack.domain.model.log.DataSyncLog
import researchstack.domain.usecase.log.AppLogger
import researchstack.presentation.pref.UIPreference

abstract class NetworkAwareWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (!isAllowedNetwork()) {
            Log.i("NetworkAwareWorker", "skip worker: require wifi network")
            AppLogger.saveLog(DataSyncLog("skip worker: require wifi network"))
            return Result.success()
        }
        return doTask()
    }

    protected suspend fun isAllowedNetwork(): Boolean {
        val mobileDataSyncEnabled = UIPreference(applicationContext.dataStore).mobileDataSyncEnabled.first()
        if (mobileDataSyncEnabled) return true

        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ?: false
    }

    abstract suspend fun doTask(): Result
}
