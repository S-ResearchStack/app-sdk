package researchstack.data.datasource.grpc

import com.google.protobuf.Empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import researchstack.backend.integration.outport.TaskOutPort
import researchstack.data.datasource.grpc.mapper.toEntitiesFlow
import researchstack.data.datasource.grpc.mapper.toGrpcData
import researchstack.data.datasource.grpc.mapper.toTimeStamp
import researchstack.data.datasource.local.room.entity.TaskEntity
import researchstack.domain.model.task.taskresult.TaskResult
import java.time.LocalDateTime

class GrpcTaskDataSource(private val taskOutPort: TaskOutPort) {
    suspend fun getAllNewTask(lastSyncTime: LocalDateTime): Result<List<Flow<TaskEntity>>> = withContext(Dispatchers.IO) {
        taskOutPort.getAllNewTaskSpecs(lastSyncTime.toTimeStamp()).map { it.taskSpecsList.map { task -> task.toEntitiesFlow() } }
    }

    suspend fun getTaskOfStudy(studyId: String): Result<List<Flow<TaskEntity>>> = withContext(Dispatchers.IO) {
        taskOutPort.getTaskSpecs(studyId).map { it.taskSpecsList.map { task -> task.toEntitiesFlow() } }
    }

    suspend fun getAllMyTaskSpec(): Result<List<Flow<TaskEntity>>> = withContext(Dispatchers.IO) {
        taskOutPort.getAllTaskSpecs().map { it.taskSpecsList.map { task -> task.toEntitiesFlow() } }
    }

    suspend fun uploadResults(studyId: String, taskId: String, taskResult: TaskResult): Result<Empty> =
        taskOutPort.uploadTaskResult(taskResult.toGrpcData(studyId, taskId))
}
