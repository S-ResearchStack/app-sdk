package researchstack.data.repository

import io.grpc.Status
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.Question
import researchstack.backend.grpc.Section
import researchstack.backend.grpc.SurveyTask
import researchstack.data.datasource.grpc.GrpcStudyDataSource
import researchstack.data.datasource.grpc.mapper.toDomain
import researchstack.data.datasource.local.room.dao.ParticipationRequirementDao
import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity
import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity.InformedConsentEntity
import researchstack.data.repository.study.ParticipationRequirementRepositoryImpl
import researchstack.domain.model.eligibilitytest.answer.ChoiceAnswer
import researchstack.domain.model.sensor.TrackerDataType.LIGHT
import researchstack.domain.model.shealth.SHealthDataType.HEART_RATE
import researchstack.domain.model.task.question.TextQuestion
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.QuestionTag.TEXT
import researchstack.backend.grpc.EligibilityTest as GrpcEligibilityTest
import researchstack.backend.grpc.InformedConsent as GrpcInformedConsent
import researchstack.domain.model.task.Section as SectionModel

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class ParticipationRequirementRepositoryImplTest {

    private val participationRequirementDao = spyk<ParticipationRequirementDao>()
    private val grpcStudyDataSource = mockk<GrpcStudyDataSource>()
    private val participationRequirementRepository =
        ParticipationRequirementRepositoryImpl(participationRequirementDao, grpcStudyDataSource)

    private val studyId = "study id"
    private val imageUrl = "image url"

    private val testParticipationRequirementListResponse =
        GetParticipationRequirementListResponse.newBuilder().setInformedConsent(
            GrpcInformedConsent.newBuilder().setImagePath(imageUrl)
        ).setEligibilityTest(
            GrpcEligibilityTest.newBuilder()
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

    private val testParticipationRequirement = testParticipationRequirementListResponse.toDomain("")

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchParticipationRequirementFromNetwork should save all participation requirement into local repository`() =
        runTest {
            coEvery { grpcStudyDataSource.getParticipationRequirement(studyId) } returns Result.success(testParticipationRequirement)

            participationRequirementRepository.fetchParticipationRequirementFromNetwork(studyId)

            coVerify(exactly = 1) {
                participationRequirementDao.insertAll(any())
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchParticipationRequirementFromNetwork should not throw exception`() = runTest {
        coEvery { grpcStudyDataSource.getParticipationRequirement(studyId) } returns Result.failure(
            Status.UNKNOWN.asException()
        )

        assertDoesNotThrow {
            participationRequirementRepository.fetchParticipationRequirementFromNetwork(
                studyId
            )
        }

        coVerify(exactly = 0) { participationRequirementDao.insertAll(any()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getParticipationRequirement should return failure if fail to get the participation-requirement`() = runTest {
        coEvery { grpcStudyDataSource.getParticipationRequirement(studyId) } returns Result.success(testParticipationRequirement)

        coEvery { participationRequirementDao.getParticipationRequirement(any()) } returns null

        assertTrue(
            participationRequirementRepository.getParticipationRequirement("studyId")
                .isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getParticipationRequirement should return the participation-requirement of the study`() = runTest {
        coEvery { grpcStudyDataSource.getParticipationRequirement(studyId) } returns Result.success(testParticipationRequirement)

        val participationRequirementEntity = ParticipationRequirementEntity(
            studyId = "studyId",
            SHealthDataTypes = listOf(HEART_RATE),
            trackerDataTypes = listOf(LIGHT),
            privDataTypes = emptyList(),
            deviceStatDataTypes = emptyList(),
            sections = listOf(
                SectionModel(
                    listOf(
                        TextQuestion(
                            "qid4",
                            "rank",
                            "ex",
                            TEXT,
                            true,
                        ),
                    )
                )
            ),
            answers = listOf(
                ChoiceAnswer(
                    "questionId",
                    listOf(Option("1", "label"))
                )
            ),
            surveyResult = null,
            informedConsentEntity = InformedConsentEntity("https://consent.url"),
        )

        coEvery { participationRequirementDao.getParticipationRequirement(any()) } returns
            participationRequirementEntity

        val participationRequirement = participationRequirementRepository.getParticipationRequirement("studyId")
            .getOrThrow()

        assertEquals(participationRequirementEntity.SHealthDataTypes, participationRequirement.SHealthDataTypes)
        assertEquals(participationRequirementEntity.trackerDataTypes, participationRequirement.trackerDataTypes)
        assertEquals(participationRequirementEntity.sections, participationRequirement.eligibilityTest.sections)
        assertEquals(participationRequirementEntity.answers, participationRequirement.eligibilityTest.answers)
        assertEquals(
            participationRequirementEntity.informedConsentEntity.informedConsentUrl,
            participationRequirement.informedConsent.imageUrl
        )
    }
}
