package healthstack.app.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import healthstack.app.pref.dataStore

abstract class NetworkAwareWorker constructor(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (!isAllowedNetwork()) {
            Log.i("NetworkAwareWorker", "skip worker: require wifi network")
            return Result.success()
        }
        return doTask()
    }

    protected suspend fun isAllowedNetwork(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ?: false
    }

    abstract suspend fun doTask(): Result
}
