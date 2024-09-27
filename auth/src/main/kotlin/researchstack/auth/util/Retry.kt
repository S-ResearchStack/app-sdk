package researchstack.auth.util

import kotlinx.coroutines.delay

suspend fun <T> withRetry(retryCount: Int, timeMillis: Long, block: suspend () -> Result<T>): Result<T> {
    var attempt = 0
    var result: Result<T>
    do {
        result = block()
        if (result.isSuccess) return result
        delay(timeMillis)
        attempt += 1
    } while (attempt < retryCount)
    return result
}
