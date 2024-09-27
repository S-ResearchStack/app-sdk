package researchstack.backend.integration.outport

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import researchstack.backend.grpc.TaskResult
import researchstack.backend.grpc.TaskSpecResponse

interface TaskOutPort {
    suspend fun getAllNewTaskSpecs(lastSyncTime: Timestamp): Result<TaskSpecResponse>

    suspend fun getTaskSpecs(studyId: String): Result<TaskSpecResponse>

    suspend fun getAllTaskSpecs(): Result<TaskSpecResponse>

    suspend fun uploadTaskResult(taskResult: TaskResult): Result<Empty>
}
