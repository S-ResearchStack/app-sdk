package researchstack.domain.repository

import researchstack.domain.model.log.AppLog

interface LogRepository {
    suspend fun saveAppLog(appLog: AppLog): Result<Unit>
    suspend fun sendAppLogToSever(): Result<Unit>

    // TODO pagination
    suspend fun getLatestLogs(name: String, count: Int): List<AppLog>
}
