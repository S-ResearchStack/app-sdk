package researchstack.auth.data.repository

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import researchstack.auth.domain.model.IdToken

object IdTokenLock {

    private val lock = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    suspend fun tryLock(
        onLockAcquired: suspend () -> Result<IdToken>,
        onLockNotAcquired: suspend () -> Result<IdToken>,
    ): Result<IdToken> {
        return withContext(Dispatchers.IO) {
            if (lock.tryLock()) {
                try {
                    onLockAcquired()
                } finally {
                    lock.unlock()
                }
            } else {
                onLockNotAcquired()
            }
        }
    }
}
