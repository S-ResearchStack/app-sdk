package researchstack.requiredinterfaces

import researchstack.domain.model.log.AppLog

interface AppLoggable {
    suspend fun saveLog(appLog: AppLog)
}
