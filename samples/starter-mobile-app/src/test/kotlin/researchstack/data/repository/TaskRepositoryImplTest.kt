package researchstack.data.repository

import androidx.work.WorkManager
import androidx.work.WorkRequest
import io.grpc.Status
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.grpc.ActivityTask
import researchstack.backend.grpc.Question
import researchstack.backend.grpc.Section
import researchstack.backend.grpc.SurveyTask
import researchstack.backend.grpc.TaskSpec
import researchstack.backend.grpc.TaskSpecResponse
import researchstack.backend.integration.outport.TaskOutPort
import researchstack.data.datasource.grpc.GrpcTaskDataSource
import researchstack.data.datasource.grpc.mapper.toDomain
import researchstack.data.datasource.grpc.mapper.toTimeStamp
import researchstack.data.datasource.local.room.dao.TaskDao
import researchstack.data.datasource.local.room.entity.TaskEntity
import researchstack.data.datasource.local.room.mapper.toDomain
import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDate
import java.time.LocalDateTime
import researchstack.domain.model.task.SurveyTask as SurveyTaskModel

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class TaskRepositoryImplTest {

    private val taskDao = spyk<TaskDao>()
    private val taskOutPort = mockk<TaskOutPort>()

    private val workerManager = mockk<WorkManager>()
    private val taskRepository =
        TaskRepositoryImpl(workerManager, taskDao, GrpcTaskDataSource(taskOutPort))

    private val studyId = "study id"
    private val activityTaskId = "task a"
    private val surveyTaskId = "task b"
    private val surveyTask = SurveyTask.newBuilder()
        .addSections(
            0,
            Section.newBuilder().addQuestions(
                0,
                Question.newBuilder()
                    .setId("question")
                    .setTitle("title")
                    .setExplanation("explanation")
                    .setTagValue(7)
                    .setTextProperties(
                        Question.TextProperties.newBuilder().build()
                    )
            )
        )
        .build()

    private val taskSpecResponse: TaskSpecResponse = TaskSpecResponse.newBuilder().addAllTaskSpecs(
        listOf(
            TaskSpec.newBuilder().setId(activityTaskId).setStudyId(studyId).setTitle("title")
                .setDescription("des")
                .setSchedule("0 0 12 * * ?")
                .setStartTime(LocalDateTime.now().toTimeStamp())
                .setEndTime(LocalDateTime.now().plusDays(1).toTimeStamp())
                .setValidMin(120L)
                .setActivityTask(
                    ActivityTask.newBuilder()
                        .setCompletionTitle("comt")
                        .setCompletionDescription("comd")
                        .setTypeValue(1)
                        .build()
                ).build(),
            TaskSpec.newBuilder()
                .setId(surveyTaskId)
                .setStudyId(studyId)
                .setTitle("title")
                .setDescription("des")
                .setSchedule("0 0 12 * * ?")
                .setStartTime(LocalDateTime.now().toTimeStamp())
                .setEndTime(LocalDateTime.now().plusDays(1).toTimeStamp())
                .setValidMin(120L)
                .setSurveyTask(surveyTask)
                .build()
        )
    ).build()

    private val taskEntity = TaskEntity(
        id = 1,
        taskId = "taskId",
        scheduledAt = LocalDateTime.now().minusDays(1),
        validUntil = LocalDateTime.now().plusDays(1),
        studyId = studyId,
        task = SurveyTaskModel(
            id = null,
            taskId = "task-id",
            studyId = studyId,
            title = "survey-task",
            description = "survey-description",
            scheduledAt = LocalDateTime.now().minusDays(1),
            validUntil = LocalDateTime.now().plusDays(1),
            inClinic = true,
            sections = surveyTask.sectionsList.map { section -> section.toDomain() },
            surveyResult = null
        ),
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchTasksFromNetwork should save all tasks into local repository`() = runTest {
        coEvery { taskOutPort.getAllNewTaskSpecs(any()) } returns Result.success(taskSpecResponse)

        val taskEntitiesSlot = slot<List<TaskEntity>>()
        coJustRun { taskDao.insertAll(capture(taskEntitiesSlot)) }

        val result = taskRepository.fetchNewTasks(LocalDateTime.now())

        assertTrue(result.isSuccess)

        coVerify(exactly = taskSpecResponse.taskSpecsList.size) { taskDao.insertAll(any()) }

        val taskEntity = taskEntitiesSlot.captured[0]
        assertNull(taskEntity.id)
        assertTrue(taskEntity.taskId.isNotBlank())
        assertNotNull(taskEntity.scheduledAt)
        assertNotNull(taskEntity.validUntil)
        assertNull(taskEntity.taskResult)
        assertNull(taskEntity.finishedDate)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchStudiesFromNetwork should not throw exception`() = runTest {
        coEvery { taskOutPort.getAllNewTaskSpecs(any()) } returns Result.failure(Status.UNKNOWN.asException())

        assertDoesNotThrow { taskRepository.fetchNewTasks(LocalDateTime.now()) }

        coVerify(exactly = 0) { taskDao.insertAll(any()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchTasksOfStudy should not throw exception`() = runTest {
        coEvery { taskOutPort.getTaskSpecs(any()) } returns Result.success(taskSpecResponse)
        coEvery { taskDao.insertAll(any()) } throws RuntimeException()

        assertDoesNotThrow { taskRepository.fetchTasksOfStudy("study-id") }

        coVerify(exactly = 1) { taskDao.insertAll(any()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchMyTasks should not throw exception`() = runTest {
        coEvery { taskOutPort.getAllTaskSpecs() } returns Result.success(taskSpecResponse)
        coEvery { taskDao.insertAll(any()) } throws RuntimeException()

        assertDoesNotThrow { taskRepository.fetchMyTasks() }

        coVerify(exactly = 1) { taskDao.insertAll(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getActiveTask should return current active tasks`() = runTest {
        every { taskDao.getActiveTasks(any()) } returns flowOf(listOf(taskEntity))

        taskRepository.getActiveTasks(LocalDateTime.now())
            .collect {
                assertEquals(taskEntity.toDomain(), it.first())
            }

        coVerify(exactly = 1) { taskDao.getActiveTasks(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUpcomingTasks should return upcoming tasks`() = runTest {
        every { taskDao.getUpcomingTasks(any(), any()) } returns flowOf(listOf(taskEntity))

        taskRepository.getUpcomingTasks(LocalDateTime.now())
            .collect {
                assertEquals(taskEntity.toDomain(), it.first())
            }

        coVerify(exactly = 1) { taskDao.getUpcomingTasks(any(), any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getCompletedTasks should return current active tasks`() = runTest {
        every { taskDao.getCompletedTasks(any()) } returns flowOf(listOf(taskEntity))

        taskRepository.getCompletedTasks(LocalDate.now())
            .collect {
                assertEquals(taskEntity.toDomain(), it.first())
            }

        coVerify(exactly = 1) { taskDao.getCompletedTasks(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `removeAll should invoke removeAll of TaskDao`() = runTest {
        coJustRun { taskDao.removeAll() }

        taskRepository.removeAllTasks()

        coVerify(exactly = 1) { taskDao.removeAll() }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeAll should catch exception if it is thrown exception`() = runTest {
        coEvery { taskDao.removeAll() } throws RuntimeException()

        assertDoesNotThrow {
            val result = taskRepository.removeAllTasks()
            assertTrue(result.isFailure)
        }

        coVerify(exactly = 1) { taskDao.removeAll() }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `saveResult should catch exception if it is thrown exception`() = runTest {
        coEvery { taskDao.setResult(any(), any(), any()) } throws RuntimeException()
        taskEntity.taskResult = SurveyResult(
            id = 1,
            startedAt = LocalDateTime.now(),
            finishedAt = LocalDateTime.now(),
            questionResults = emptyList()
        )
        assertDoesNotThrow {
            val result = taskRepository.saveResult(taskEntity.taskResult!!)
            assertTrue(result.isFailure)
        }

        coVerify(exactly = 1) { taskDao.setResult(any(), any(), any()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `uploadTaskResult should catch exception if it is thrown exception`() = runTest {
        every { workerManager.enqueue(any<WorkRequest>()) } throws RuntimeException()

        val taskEntityId = 1

        assertDoesNotThrow {
            val result = taskRepository.uploadTaskResult(taskEntityId)
            assertTrue(result.isFailure)
        }
    }
}
