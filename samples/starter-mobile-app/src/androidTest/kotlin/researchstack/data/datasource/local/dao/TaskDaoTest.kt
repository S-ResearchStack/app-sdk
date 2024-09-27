package researchstack.data.datasource.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.runner.RunWith
import researchstack.backend.grpc.ActivityTask
import researchstack.backend.grpc.Question
import researchstack.backend.grpc.Section
import researchstack.backend.grpc.SurveyTask
import researchstack.backend.grpc.TaskSpec
import researchstack.backend.grpc.TaskSpecResponse
import researchstack.data.datasource.grpc.mapper.toEntitiesFlow
import researchstack.data.datasource.grpc.mapper.toTimeStamp
import researchstack.data.datasource.local.room.ResearchAppDatabase
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.taskresult.ActivityResult
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val taskDao = ResearchAppDatabase.getDatabase(context).taskDao()
    private val studyId = "study id"
    private val activityTaskId = "task a"
    private val surveyTaskId = "task b"
    private val taskSpecResponse: TaskSpecResponse = TaskSpecResponse.newBuilder().addAllTaskSpecs(
        listOf(
            TaskSpec.newBuilder().setId(activityTaskId).setStudyId(studyId).setTitle("title")
                .setDescription("des")
                .setSchedule("0 0 12 * * ?")
                .setStartTime(LocalDateTime.now().toTimeStamp())
                .setEndTime(LocalDateTime.now().plusDays(20).toTimeStamp())
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
                .setEndTime(LocalDateTime.now().plusDays(20).toTimeStamp())
                .setValidMin(120L)
                .setSurveyTask(
                    SurveyTask.newBuilder()
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
                ).build()
        )
    ).build()

    @Test
    fun crudTest() {
        assertDoesNotThrow {
            runBlocking { taskDao.insertAll(taskSpecResponse.taskSpecsList.flatMap { listOf(it.toEntitiesFlow().first()) }) }
            runBlocking {
                taskDao.setResult(
                    1,
                    ActivityResult(1, LocalDateTime.now(), LocalDateTime.now(), ActivityType.STROOP_TEST, emptyMap()),
                    LocalDateTime.now().toString()
                )
            }
            runBlocking { taskDao.findAll() }
            runBlocking { taskDao.removeAll() }
        }
    }
}
