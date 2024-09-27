package researchstack.data.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.samsung.android.service.health.tracking.ConnectionListener
import com.samsung.android.service.health.tracking.HealthTrackerException
import com.samsung.android.service.health.tracking.HealthTrackingService

object PrivDataRequester {
    var isConnected: Boolean = false
    lateinit var connectionListener: ConnectionListener

    @SuppressLint("StaticFieldLeak")
    lateinit var healthTrackingService: HealthTrackingService

    fun initialize(
        activity: Activity?,
        context: Context,
        onConnectionSuccess: () -> Unit = {},
        onConnectionEnded: () -> Unit = {},
        onConnectionFailed: () -> Unit = {},
    ) {
        connectionListener = object : ConnectionListener {
            override fun onConnectionSuccess() {
                Log.i(PrivDataRequester::class.simpleName, "success to connect HealthTrackingService")
                isConnected = true
                onConnectionSuccess()
            }

            override fun onConnectionEnded() {
                isConnected = false
                onConnectionEnded()
            }

            override fun onConnectionFailed(e: HealthTrackerException) {
                isConnected = false
                if (e.hasResolution()) e.resolve(activity)
                onConnectionFailed()
            }
        }

        healthTrackingService =
            HealthTrackingService(connectionListener, context)
    }
}
