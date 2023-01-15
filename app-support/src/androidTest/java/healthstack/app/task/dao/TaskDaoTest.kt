package healthstack.app.task.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import healthstack.app.task.converter.LocalDateTimeConverter
import healthstack.app.task.converter.PropertiesTypeConverter
import healthstack.app.task.converter.ResultConverter
import healthstack.app.task.db.TaskRoomDatabase
import healthstack.app.task.entity.Task
import healthstack.app.task.entity.Task.Properties
import healthstack.app.task.entity.Task.Result
import healthstack.backend.integration.task.ChoiceProperties
import healthstack.backend.integration.task.Contents
import healthstack.backend.integration.task.Item
import healthstack.backend.integration.task.Option
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@TestInstance(PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class TaskDaoTest {
    private lateinit var taskDao: TaskDao
    private lateinit var db: TaskRoomDatabase
    private val curTime = LocalDateTime.now()
    private val exampleTask = Task(
        id = 1,
        revisionId = 1,
        taskId = "taskId",
        properties = Properties(
            title = "Daily Survey",
            description = "Daily",
            items = listOf(
                Item(
                    name = "Question0",
                    type = "Question",
                    contents = Contents(
                        title = "good morning",
                        required = true,
                        type = "CHOICE",
                        itemProperties = ChoiceProperties(
                            tag = "RADIO",
                            options = listOf(Option("value"))
                        )
                    ),
                    sequence = 0
                )
            )
        ),
        result = null,
        createdAt = curTime,
        scheduledAt = curTime.plusMinutes(5),
        validUntil = curTime.plusMinutes(10),
        submittedAt = null,
        startedAt = null
    )

    @BeforeAll
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskRoomDatabase::class.java)
            .addTypeConverter(PropertiesTypeConverter())
            .addTypeConverter(LocalDateTimeConverter())
            .addTypeConverter(ResultConverter(Gson()))
            .build()
        taskDao = db.taskDao()
    }

    @AfterEach
    fun removeTestData() {
        db.clearAllTables()
    }

    @AfterAll
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAllAndFindByIdTest() {
        runTest {
            taskDao.insertAll(listOf(exampleTask))

            val storedTask = taskDao.findById(exampleTask.id.toString()).first()

            assertEquals(exampleTask.taskId, storedTask.taskId)
            assertEquals(exampleTask.revisionId, storedTask.revisionId)
            assertEquals(exampleTask.validUntil, storedTask.validUntil)
            assertEquals(exampleTask.properties.title, storedTask.properties.title)
            assertEquals(exampleTask.properties.items[0].name, storedTask.properties.items[0].name)
            assertEquals(
                exampleTask.properties.items[0].contents.title,
                storedTask.properties.items[0].contents.title
            )
            assertEquals(
                exampleTask.properties.items[0].contents.itemProperties.tag,
                storedTask.properties.items[0].contents.itemProperties.tag
            )
            assertEquals(
                exampleTask.properties.items[0].contents.itemProperties::class,
                storedTask.properties.items[0].contents.itemProperties::class
            )
        }
    }

    @Test
    fun setSubmittedAtTest() {
        val submitTime = LocalDateTime.now()

        runTest {
            taskDao.insertAll(listOf(exampleTask))
            taskDao.setSubmittedAt(exampleTask.id.toString(), submitTime.toString())

            val storedTask = taskDao.findById(exampleTask.id.toString()).first()

            assertEquals(submitTime, storedTask.submittedAt)
        }
    }

    @Test
    fun setResultTest() {
        val result = Result("questionId", "response")
        val startTime = LocalDateTime.now()
        val submitTime = startTime.plusMinutes(5)

        runTest {
            taskDao.insertAll(listOf(exampleTask))
            taskDao.setResult(exampleTask.id.toString(), listOf(result), startTime.toString(), submitTime.toString())

            val storedTask = taskDao.findById(exampleTask.id.toString()).first()

            assertEquals(listOf(result), storedTask.result)
            assertEquals(startTime, storedTask.startedAt)
            assertEquals(submitTime, storedTask.submittedAt)
        }
    }

    @Test
    fun removeAllTest() {
        runTest {
            taskDao.insertAll(listOf(exampleTask))

            var storedTask = taskDao.findById(exampleTask.id.toString()).first()
            assertEquals(exampleTask.taskId, storedTask.taskId)

            taskDao.removeAll()
            storedTask = taskDao.findById(exampleTask.id.toString()).first()
            assertEquals(null, storedTask)
        }
    }

    @Test
    fun getActiveTasksTest() {
        runTest {
            taskDao.insertAll(listOf(exampleTask))
            val storedTask = taskDao.getActiveTasks(curTime.plusMinutes(7).toString()).first()

            assertEquals(exampleTask.taskId, storedTask[0].taskId)
        }
    }

    @Test
    fun getCompletedTasksTest() {
        val submitTime = curTime.plusMinutes(7)

        runTest {
            taskDao.insertAll(listOf(exampleTask))
            taskDao.setSubmittedAt(exampleTask.id.toString(), submitTime.toString())
            val storedTask =
                taskDao.getCompletedTasks(curTime.plusMinutes(5).toString(), curTime.plusMinutes(10).toString()).first()

            assertEquals(exampleTask.taskId, storedTask[0].taskId)
        }
    }

    @Test
    fun getUpcomingTasksTest() {

        runTest {
            taskDao.insertAll(listOf(exampleTask))
            val storedTask =
                taskDao.getUpcomingTasks(curTime.plusMinutes(3).toString(), curTime.plusMinutes(7).toString()).first()

            assertEquals(exampleTask.taskId, storedTask[0].taskId)
        }
    }
}
