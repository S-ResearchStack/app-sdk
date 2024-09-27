package researchstack.auth.data.datasource.auth

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.msc.sa.aidl.ISAService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import researchstack.auth.data.datasource.auth.samsung.IdTokenListener
import researchstack.auth.data.datasource.auth.samsung.SACallbackHandler
import researchstack.auth.data.repository.auth.samsung.SAStringResource.SA_ACCOUNT_TYPE

object SAIdTokenRequester {
    private const val TIMEOUT = 2000L
    private const val DELAY_TIME = 10L
    private const val REQUEST_ID = 100
    private const val SA_BIND_INTENT = "com.msc.action.samsungaccount.REQUEST_SERVICE"
    private const val SA_REQUEST_CLASS_NAME = "com.msc.sa.service.RequestService"
    private val TAG = SAIdTokenRequester::class.simpleName
    private var isBound = false
    private lateinit var saService: ISAService
    private val saCallbackHandler: SACallbackHandler = SACallbackHandler()
    private var registrationCode: String? = null
    private lateinit var packageName: String
    private lateinit var CLIENT_ID: String
    private lateinit var CLIENT_SECRET: String

    fun initialize(context: Context, clientId: String, clientSecret: String) {
        if (isBound) {
            return
        }
        packageName = context.packageName
        CLIENT_ID = clientId
        CLIENT_SECRET = clientSecret

        CoroutineScope(Dispatchers.IO).launch {
            bind(context)
        }
    }

    fun registerIdTokenListener(listener: IdTokenListener) {
        saCallbackHandler.registerIdTokenListener(listener)
    }

    private fun bind(context: Context) {
        context.bindService(
            Intent(SA_BIND_INTENT).apply { setClassName(SA_ACCOUNT_TYPE, SA_REQUEST_CLASS_NAME) },
            serviceConnection,
            BIND_AUTO_CREATE
        )
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            runCatching {
                saService = ISAService.Stub.asInterface(service)
                for (i in 0..1) {
                    registrationCode = saService
                        .registerCallback(CLIENT_ID, CLIENT_SECRET, packageName, saCallbackHandler)
                    Log.i(
                        TAG, "SamsungAccount AIDL Registration Code - ${registrationCode ?: "null"}"
                    )
                    if (registrationCode != null) break
                }
            }.onFailure {
                Log.e(TAG, "Fail to bind service - ${it.message}")
                isBound = false
            }.onSuccess {
                Log.i(TAG, "Success to bind service")
                isBound = true
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            saService.unregisterCallback(registrationCode)
            registrationCode = null
            isBound = false
        }
    }

    fun requestIdToken() {
        waitUntilBindingService()
        val data = Bundle().apply { putString("scope", "openid") }
        saService.requestAuthCode(REQUEST_ID, registrationCode, data)
    }

    private fun waitUntilBindingService() {
        runBlocking(Dispatchers.IO) {
            try {
                withTimeout(TIMEOUT) {
                    while (!isBound) delay(DELAY_TIME)
                }
            } catch (_: TimeoutCancellationException) {
                throw IllegalStateException("SAAuthRepository is not initialized yet")
            }
        }
    }
}
