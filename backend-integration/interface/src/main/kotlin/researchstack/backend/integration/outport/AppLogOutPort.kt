package researchstack.backend.integration.outport

import researchstack.backend.grpc.AppLog

interface AppLogOutPort {
    suspend fun sendAppLog(appLog: AppLog): Result<Unit>
}
