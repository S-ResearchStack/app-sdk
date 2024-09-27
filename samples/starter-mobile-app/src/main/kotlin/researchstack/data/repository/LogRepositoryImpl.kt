package researchstack.data.repository

import android.util.Log
import com.google.protobuf.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.auth.domain.usecase.GetAccountUseCase
import researchstack.backend.integration.outport.AppLogOutPort
import researchstack.data.datasource.local.room.dao.LogDao
import researchstack.data.datasource.local.room.entity.AppLogEntity
import researchstack.domain.model.log.AppLog
import researchstack.domain.repository.LogRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import researchstack.backend.grpc.AppLog as GrpcAppLog

class LogRepositoryImpl(
    private val appLogOutPort: AppLogOutPort,
    private val logDao: LogDao,
    private val getAccountUseCase: GetAccountUseCase,
) : LogRepository {
    override suspend fun saveAppLog(appLog: AppLog): Result<Unit> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                logDao.insert(appLog.toEntity())
            }
        }

    override suspend fun sendAppLogToSever(): Result<Unit> =
        withContext(Dispatchers.IO) {
            val listLogs = logDao.findUnsentLogs()
            val id = getAccountUseCase().getOrNull()?.id
            for (logEntity in listLogs) {
                kotlin.runCatching {
                    appLogOutPort.sendAppLog(logEntity.toGrpcAppLog(id))
                }.onSuccess {
                    logEntity.id?.let { id ->
                        logDao.setLogAsSent(id)
                    }
                    logDao.deleteSentLogsBefore(
                        Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()
                    )
                }.getOrElse {
                    Log.i(LogRepositoryImpl::class.simpleName, it.stackTraceToString())
                    return@withContext Result.failure<Unit>(it)
                }
            }
            Result.success(Unit)
        }

    override suspend fun getLatestLogs(name: String, count: Int): List<AppLog> =
        withContext(Dispatchers.IO) {
            logDao.getLatestLogs(name, count)
                .map {
                    AppLog(it.name)
                        .apply {
                            put("time", Instant.ofEpochMilli(it.timeStamp).toString())
                            it.data.forEach { (k, v) ->
                                put(k, v)
                            }
                        }
                }
        }
}

fun AppLog.toEntity(): AppLogEntity =
    AppLogEntity(
        id = null,
        name = name ?: "",
        timeStamp = Instant.now().toEpochMilli(),
        data = getData()
    )

fun AppLogEntity.toGrpcAppLog(id: String?): GrpcAppLog = GrpcAppLog.newBuilder().apply {
    name = this@toGrpcAppLog.name
    putAllData(this@toGrpcAppLog.data)
    id?.let { putData("id", it) }
    timestamp = Timestamp.newBuilder().apply {
        seconds = Instant.ofEpochMilli(this@toGrpcAppLog.timeStamp).epochSecond
        nanos = Instant.ofEpochMilli(this@toGrpcAppLog.timeStamp).nano
    }.build()
}.build()
