package researchstack.backend.integration.adapter.outgoing

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import researchstack.backend.grpc.AllNewTaskRequest
import researchstack.backend.grpc.TaskResult
import researchstack.backend.grpc.TaskResultUploadRequest
import researchstack.backend.grpc.TaskServiceGrpcKt
import researchstack.backend.grpc.TaskSpecRequest
import researchstack.backend.grpc.TaskSpecResponse
import researchstack.backend.integration.outport.TaskOutPort
import javax.inject.Inject

class TaskAdapter @Inject constructor(
    private val taskServiceCoroutineStub: TaskServiceGrpcKt.TaskServiceCoroutineStub
) : TaskOutPort {
    override suspend fun getAllNewTaskSpecs(lastSyncTime: Timestamp): Result<TaskSpecResponse> = runCatching {
        taskServiceCoroutineStub.getAllNewTaskSpecs(
            AllNewTaskRequest.newBuilder()
            .setLastSyncTime(lastSyncTime)
            .build()
        )
    }

    override suspend fun getTaskSpecs(studyId: String): Result<TaskSpecResponse> = runCatching {
        taskServiceCoroutineStub.getTaskSpecs(
            TaskSpecRequest.newBuilder()
                .setStudyId(studyId)
                .build()
        )
    }

    override suspend fun getAllTaskSpecs(): Result<TaskSpecResponse> = runCatching {
        taskServiceCoroutineStub.getAllTaskSpecs(Empty.getDefaultInstance())
    }

    override suspend fun uploadTaskResult(taskResult: TaskResult): Result<Empty> = runCatching {
        taskServiceCoroutineStub.uploadTaskResult(
            TaskResultUploadRequest.newBuilder().setTaskResult(taskResult).build()
        )
    }
}
