package researchstack.domain.usecase.log

import researchstack.domain.model.log.AppLog
import researchstack.domain.repository.LogRepository
import researchstack.requiredinterfaces.AppLoggable

object AppLogger : AppLoggable {
    private lateinit var logRepository: LogRepository

    fun initialize(logRepository: LogRepository) {
        AppLogger.logRepository = logRepository
    }

    override suspend fun saveLog(appLog: AppLog) {
        if (this::logRepository.isInitialized) {
            logRepository.saveAppLog(appLog)
        }
    }
}
