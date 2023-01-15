package healthstack.app.task.repository

import healthstack.app.task.dao.TaskDao
import healthstack.app.task.db.TaskRoomDatabase
import healthstack.app.task.entity.Task
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Task RepositoryImpl Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImplTest {
    private val taskDaoStub = mockk<TaskDao>(relaxed = true)
    private val taskRoomDatabaseStub = mockk<TaskRoomDatabase>()
    private lateinit var taskRepositoryImpl: TaskRepositoryImpl

    @BeforeAll
    fun beforeAll() {
        mockkObject(TaskRoomDatabase)
        every { TaskRoomDatabase.getInstance() } returns taskRoomDatabaseStub
        every { taskRoomDatabaseStub.taskDao() } returns taskDaoStub

        taskRepositoryImpl = TaskRepositoryImpl()
    }

    @Tag("positive")
    @Test
    fun `test insertAll api`() {
        val targetTasks = emptyList<Task>()

        runTest {
            taskRepositoryImpl.insertAll(targetTasks)

            coVerify {
                taskDaoStub.insertAll(targetTasks)
            }
        }
    }

    @Tag("positive")
    @Test
    fun `test getActiveTasks api`() {
        val targetTime = LocalDateTime.now()

        runTest {
            taskRepositoryImpl.getActiveTasks(targetTime)

            coVerify {
                taskDaoStub.getActiveTasks(targetTime.toString())
            }
        }
    }

    @Tag("positive")
    @Test
    fun `test getUpcomingTasks api`() {
        val targetTime = LocalDateTime.now()

        runTest {
            taskRepositoryImpl.getUpcomingTasks(targetTime)

            coVerify {
                taskDaoStub.getUpcomingTasks(
                    targetTime.toString(),
                    targetTime.toLocalDate().plusDays(1).toString()
                )
            }
        }
    }

    @Tag("positive")
    @Test
    fun `test getCompletedTasks api`() {
        val targetTime = LocalDate.now()

        runTest {
            taskRepositoryImpl.getCompletedTasks(targetTime)

            coVerify {
                taskDaoStub.getCompletedTasks(
                    targetTime.toString(),
                    targetTime.plusDays(1).toString()
                )
            }
        }
    }
}
