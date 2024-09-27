package researchstack.data.repository

import androidx.work.Data
import androidx.work.ListenableWorker.Result.Failure
import androidx.work.ListenableWorker.Result.Retry
import androidx.work.ListenableWorker.Result.Success
import androidx.work.WorkerParameters
import androidx.work.impl.utils.taskexecutor.TaskExecutor
import com.google.protobuf.Empty
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.integration.outport.TaskOutPort
import researchstack.data.datasource.grpc.GrpcTaskDataSource
import researchstack.data.datasource.local.room.dao.TaskDao
import researchstack.data.datasource.local.room.entity.TaskEntity
import researchstack.data.repository.TaskRepositoryImpl.UploadWorker
import researchstack.domain.model.task.SurveyTask
import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDateTime

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class UploadWorkerTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private val executor = mockk<TaskExecutor> {
        every { serialTaskExecutor } returns mockk()
    }

    private val workerParams = mockk<WorkerParameters> {
        every { taskExecutor } returns executor
    }

    private val workerData = mockk<Data> {
        every { getInt(UploadWorker.SCHEDULED_TASK_ID_KEY, -1) } returns 1
    }

    private val taskDao = mockk<TaskDao>()
    private val taskOutPort = mockk<TaskOutPort>()

    private val uploadWorker = UploadWorker(mockk(), workerParams, taskDao, GrpcTaskDataSource(taskOutPort))

    private val taskEntity = TaskEntity(
        id = 1,
        taskId = "taskId",
        scheduledAt = LocalDateTime.now().minusDays(1),
        validUntil = LocalDateTime.now().plusDays(1),
        studyId = "studyId",
        task = SurveyTask(
            id = null,
            taskId = "task-id",
            studyId = "studyId",
            title = "survey-task",
            description = "survey-description",
            scheduledAt = LocalDateTime.now().minusDays(1),
            validUntil = LocalDateTime.now().plusDays(1),
            inClinic = true,
            sections = emptyList(),
            surveyResult = null
        ),
    )

    @BeforeEach
    fun beforeEach() {
        every { workerParams.inputData } returns workerData
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `doWork should return failure when taskId is not given`() = testScope.runTest {
        every { workerData.getInt(UploadWorker.SCHEDULED_TASK_ID_KEY, -1) } returns -1

        val result = uploadWorker.doWork()
        assertTrue(result is Failure)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `doWork should return failure when not existed taskId`() = runTest {
        coEvery { taskDao.findById(any()) } returns null

        val result = uploadWorker.doWork()
        assertTrue(result is Failure)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `doWork should return failure if task result is null`() = runTest {
        coEvery { taskDao.findById(any()) } returns taskEntity

        val result = uploadWorker.doWork()
        assertTrue(result is Failure)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `doWork should return retry if failed to upload task result`() = runTest {
        coEvery { taskDao.findById(any()) } returns taskEntity.copy(
            taskResult = SurveyResult(
                id = 1,
                startedAt = LocalDateTime.now(),
                finishedAt = LocalDateTime.now(),
                questionResults = emptyList()
            )
        )

        coEvery { taskOutPort.uploadTaskResult(any()) } throws RuntimeException()

        val result = uploadWorker.doWork()
        assertTrue(result is Retry)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `doWork should return success`() = runTest {
        coEvery { taskDao.findById(any()) } returns taskEntity.copy(
            taskResult = SurveyResult(
                id = 1,
                startedAt = LocalDateTime.now(),
                finishedAt = LocalDateTime.now(),
                questionResults = emptyList()
            )
        )

        coEvery { taskOutPort.uploadTaskResult(any()) } returns Result.success(Empty.getDefaultInstance())

        val result = uploadWorker.doWork()
        assertTrue(result is Success)
    }
}
