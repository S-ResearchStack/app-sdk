package researchstack.backend.integration.adapter.outgoing

import researchstack.backend.grpc.AppLog
import researchstack.backend.grpc.AppLogServiceGrpcKt.AppLogServiceCoroutineStub
import researchstack.backend.integration.outport.AppLogOutPort
import javax.inject.Inject

class AppLogAdapter @Inject constructor(
    private val appLogServiceCoroutineStub: AppLogServiceCoroutineStub
) : AppLogOutPort {
    override suspend fun sendAppLog(appLog: AppLog): Result<Unit> = kotlin.runCatching {
        appLogServiceCoroutineStub.sendAppLog(appLog)
    }
}
