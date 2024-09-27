package researchstack.auth.data.repository.auth

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import researchstack.auth.data.datasource.local.pref.AccountPref
import researchstack.auth.data.datasource.local.pref.BasicAuthenticationPref
import researchstack.auth.data.datasource.local.pref.IdTokenPref
import researchstack.auth.data.datasource.local.pref.dataStore
import researchstack.auth.data.repository.IdTokenLock
import researchstack.auth.data.repository.account.AccountPrefRepositoryImpl
import researchstack.auth.data.repository.idtoken.IdTokenPrefRepositoryImpl
import researchstack.auth.domain.model.Account
import researchstack.auth.domain.model.Authentication
import researchstack.auth.domain.model.BasicAuthentication
import researchstack.auth.domain.model.IdToken
import researchstack.auth.domain.repository.AuthRepository

class AuthRepositoryWrapper(
    context: Context,
    private val authRepository: AuthRepository,
) {
    private val authenticationPref = BasicAuthenticationPref(context.dataStore)
    private val accountRepository = AccountPrefRepositoryImpl(AccountPref(context.dataStore))
    private val idTokenRepository = IdTokenPrefRepositoryImpl(IdTokenPref(context.dataStore))

    suspend fun signIn(auth: Authentication): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            authRepository.signIn(auth)
                .getOrThrow()
        }.mapCatching { idToken ->
            val account = Account(idToken.subject, authRepository.getProvider())

            if (auth is BasicAuthentication) {
                authenticationPref.saveAuthInfo(auth)
            }
            accountRepository.updateAccount(account)
            idTokenRepository.updateIdToken(idToken)
        }
    }

    suspend fun getAccount(): Result<Account> = withContext(Dispatchers.IO) {
        runCatching {
            accountRepository.getAccount() ?: throw IllegalStateException("no account info. Sign in first")
        }
    }

    suspend fun getIdToken(): Result<IdToken> = withContext(Dispatchers.IO) {
        val currentToken = idTokenRepository.getIdToken()
        if (currentToken?.isExpired() == false) {
            return@withContext Result.success(currentToken)
        }
        idTokenRepository.clearIdToken()

        refreshIdToken()
    }

    private suspend fun refreshIdToken(): Result<IdToken> = withContext(Dispatchers.IO) {
        IdTokenLock.tryLock(
            onLockAcquired = {
                runCatching {
                    authenticationPref.getAuthInfo()
                        ?: throw IllegalStateException("no basic authentication info")
                }.mapCatching {
                    signIn(it).getOrThrow()
                }.mapCatching {
                    idTokenRepository.getIdToken()
                        ?: throw NoSuchElementException("fail to get idToken")
                }
            },
            onLockNotAcquired = {
                runCatching {
                    withTimeout(TIMEOUT) {
                        idTokenRepository.getIdToken() ?: throw NoSuchElementException()
                    }
                }
            }
        )
    }

    companion object {
        private const val TIMEOUT = 10000L
    }
}
