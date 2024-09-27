package researchstack.auth.data.repository.auth.samsung

import android.accounts.AccountManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import org.jetbrains.annotations.VisibleForTesting
import researchstack.auth.data.datasource.auth.SAIdTokenRequester
import researchstack.auth.data.datasource.auth.samsung.IdTokenListener
import researchstack.auth.domain.model.Authentication
import researchstack.auth.domain.model.IdToken
import researchstack.auth.domain.repository.AuthRepository

class SamsungAccountAuthRepository(private val context: Context, private val clientId: String) : AuthRepository, IdTokenListener {
    private val idTokenResultChannel = Channel<Result<String>>()

    init {
        SAIdTokenRequester.registerIdTokenListener(this)
    }

    override fun getAuthType() = AuthRepository.AuthType.OIDC

    override fun getProvider(): String = "samsung-account"

    override suspend fun signIn(auth: Authentication): Result<IdToken> {
        getAccountName().getOrThrow()

        SAIdTokenRequester.requestIdToken()
        return try {
            withTimeout(TIMEOUT) {
                idTokenResultChannel.receive()
                    .map { IdToken(it, getProvider()) }
            }
        } catch (ex: TimeoutCancellationException) {
            Log.e(TAG, ex.stackTraceToString())
            Result.failure(ex)
        }
    }

    private fun getAccountName(): Result<String> {
        return runCatching {
            if (IS_ACCOUNT_MANAGER_AVAILABLE) {
                return@runCatching AccountManager.get(context).getAccountsByType(SAStringResource.SA_ACCOUNT_TYPE)
                    .map { it.name }
                    .firstOrNull() ?: throw IllegalStateException("fail to get samsung account")
            }

            getSamsungAccountByProvider(context)
        }.onFailure {
            Log.e(TAG, it.message.orEmpty())
        }
    }

    @VisibleForTesting
    internal fun getSamsungAccountByProvider(context: Context): String {
        val contentResolver = context.contentResolver
        val resultBundle = contentResolver.call(
            Uri.parse("content://com.samsung.android.samsungaccount.accountmanagerprovider"),
            "getSamsungAccountId",
            clientId,
            null
        )!!

        val resultCode = resultBundle.getInt("result_code", 1)
        val resultMessage = resultBundle.getString("result_message", "")

        if (resultCode != 0) throw IllegalAccessException(resultMessage)

        return resultMessage.ifEmpty { throw IllegalStateException("fail to get samsung account") }
    }

    companion object {
        private val TAG = this::class.simpleName
        private val IS_ACCOUNT_MANAGER_AVAILABLE = Build.VERSION.SDK_INT < Build.VERSION_CODES.S
        private const val TIMEOUT = 10000L
    }

    override fun onReceiveTokenResult(result: Result<String>) {
        idTokenResultChannel.trySend(result)
    }
}
