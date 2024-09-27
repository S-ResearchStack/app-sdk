package researchstack.data.repository.study

import io.grpc.Status
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.grpc.EligibilityTest
import researchstack.backend.grpc.EligibilityTest.Answer
import researchstack.backend.grpc.EligibilityTest.ChoiceAnswers
import researchstack.backend.grpc.EligibilityTest.Option
import researchstack.backend.grpc.EligibilityTest.TextAnswers
import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.HealthData.HealthDataType.UNRECOGNIZED
import researchstack.backend.integration.outport.StudyOutPort
import researchstack.data.datasource.grpc.GrpcStudyDataSource
import researchstack.data.datasource.local.room.dao.ParticipationRequirementDao
import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity
import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity.InformedConsentEntity
import researchstack.data.datasource.local.room.mapper.toDomain
import researchstack.domain.model.InformedConsent
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDateTime
import researchstack.backend.grpc.HealthData.HealthDataType as HealthDataTypeProto
import researchstack.backend.grpc.InformedConsent as InformedConsentProto

internal class ParticipationRequirementRepositoryImplTest {
    private val participationRequirementDao = mockk<ParticipationRequirementDao>()
    private val studyOutPort = mockk<StudyOutPort>()
    private val participationRequirementRepository = ParticipationRequirementRepositoryImpl(
        participationRequirementDao,
        GrpcStudyDataSource(studyOutPort)
    )

    private val requirementEntity = ParticipationRequirementEntity(
        studyId = "study-id",
        SHealthDataTypes = SHealthDataType.values().toList(),
        trackerDataTypes = TrackerDataType.values().toList(),
        privDataTypes = PrivDataType.values().toList(),
        deviceStatDataTypes = DeviceStatDataType.values().toList(),
        sections = emptyList(),
        answers = emptyList(),
        surveyResult = null,
        informedConsentEntity = InformedConsentEntity("https://consent.url")
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipationRequirement should return the participation requirement of given study in local db`() =
        runTest {
            coEvery { participationRequirementDao.getParticipationRequirement(any()) } returns requirementEntity

            val result = participationRequirementRepository.getParticipationRequirement("study-id")

            assertTrue(result.isSuccess)
            assertEquals(requirementEntity.toDomain(), result.getOrNull())
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipationRequirement should return the participation requirement of given study`() = runTest {
        var called = 0
        coEvery { participationRequirementDao.getParticipationRequirement(any()) } answers {
            if (called == 0) {
                called += 1
                null
            } else requirementEntity
        }

        val requirementEntitiesSlot = slot<List<ParticipationRequirementEntity>>()
        coJustRun { participationRequirementDao.insertAll(capture(requirementEntitiesSlot)) }

        coEvery {
            studyOutPort.getParticipationRequirementList(any())
        } returns Result.success(
            GetParticipationRequirementListResponse.newBuilder()
            .setInformedConsent(InformedConsentProto.newBuilder().setImagePath("image-url"))
            .addAllDataTypes(HealthDataTypeProto.values().toList().filter { it != UNRECOGNIZED })
            .setEligibilityTest(
                EligibilityTest.newBuilder()
                    .apply {
                        addAnswers(
                            Answer.newBuilder()
                                .setChoiceAnswers(
                                    ChoiceAnswers.newBuilder()
                                        .addOptions(Option.newBuilder().setValue("option"))
                                )
                        )
                        addAnswers(
                            Answer.newBuilder()
                                .setTextAnswers(TextAnswers.newBuilder().addAnswers("texst"))
                        )
                    }
            ).build()
        )

        val studyId = "study-id"
        val result = participationRequirementRepository.getParticipationRequirement(studyId)

        assertTrue(result.isSuccess)

        assertEquals(1, requirementEntitiesSlot.captured.size)
        assertTrue(requirementEntitiesSlot.captured[0].SHealthDataTypes.isNotEmpty())
        assertTrue(requirementEntitiesSlot.captured[0].trackerDataTypes.isNotEmpty())
        assertTrue(requirementEntitiesSlot.captured[0].informedConsentEntity.informedConsentUrl.isNotBlank())
        assertNull(requirementEntitiesSlot.captured[0].surveyResult)
        assertNull(requirementEntitiesSlot.captured[0].informedConsentEntity.signedInformedConsentUrl)
        assertTrue(requirementEntitiesSlot.captured[0].answers.isNotEmpty())
        assertEquals(studyId, requirementEntitiesSlot.captured[0].studyId)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getParticipationRequirement should return failure when not existed study id`() =
        runTest {
            coEvery { participationRequirementDao.getParticipationRequirement(any()) } returns null
            coEvery { studyOutPort.getParticipationRequirementList(any()) } returns Result.failure(Status.NOT_FOUND.asException())

            val result = participationRequirementRepository.getParticipationRequirement("study-id")

            assertTrue(result.isFailure)
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `saveParticipationRequirementResultToLocal should return failure if failed to save`() =
        runTest {
            coEvery { participationRequirementDao.setResult(any(), any(), any()) } throws RuntimeException()

            val result = saveParticipationRequirement()

            assertTrue(result.isFailure)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `saveParticipationRequirementResultToLocal should save result`() =
        runTest {
            coJustRun { participationRequirementDao.setResult(any(), any(), any()) }

            val result = saveParticipationRequirement()

            assertTrue(result.isSuccess)
        }

    private suspend fun saveParticipationRequirement() =
        participationRequirementRepository.saveParticipationRequirementResultToLocal(
            "studyid",
            EligibilityTestResult(
                "studyid",
                SurveyResult(1, LocalDateTime.now(), LocalDateTime.now(), emptyList())
            ),
            InformedConsent("studyid", "image-url")
        )
}
