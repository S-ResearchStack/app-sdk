package researchstack.data.datasource.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.runner.RunWith
import researchstack.backend.grpc.EligibilityTest
import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.Question
import researchstack.backend.grpc.Section
import researchstack.backend.grpc.SurveyTask
import researchstack.data.datasource.grpc.mapper.toEntity
import researchstack.data.datasource.local.room.ResearchAppDatabase
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class EligibilityDaoTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val participationRequirementDao =
        ResearchAppDatabase.getDatabase(context).participationRequirementDao()

    private val studyId = "study id"
    private val imageUrl = "image url"
    private val startedAt = LocalDateTime.now()

    private val testParticipationRequirementListResponse =
        GetParticipationRequirementListResponse.newBuilder().setInformedConsent(
            researchstack.backend.grpc.InformedConsent.newBuilder().setImagePath(imageUrl)
        ).setEligibilityTest(
            EligibilityTest.newBuilder()
                .addAllAnswers(listOf())
                .setSurveyTask(
                    SurveyTask.newBuilder()
                        .addSections(
                            Section.newBuilder().addQuestions(
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
                )
        ).build()

    private val questionResult = QuestionResult("question", "result")
    private val surveyResult = SurveyResult(
        0, startedAt, LocalDateTime.now(), listOf(questionResult)
    )

    private val signedImageUrl = "image url"

    @Test
    fun crudTest() {
        assertDoesNotThrow {
            runBlocking {
                participationRequirementDao.insertAll(
                    listOf(testParticipationRequirementListResponse.toEntity(studyId))
                )
            }
            runBlocking {
                participationRequirementDao.setResult(studyId, surveyResult, signedImageUrl)
            }

            runBlocking { participationRequirementDao.findAll() }

            runBlocking { participationRequirementDao.removeAll() }
        }
    }
}
