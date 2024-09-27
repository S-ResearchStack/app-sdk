package researchstack.data.repository

import io.grpc.Status
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.grpc.StudyInfo
import researchstack.backend.integration.outport.StudyOutPort
import researchstack.data.datasource.grpc.GrpcStudyDataSource
import researchstack.data.datasource.grpc.mapper.toDomain
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.entity.StudyEntity
import researchstack.data.datasource.local.room.mapper.toEntity
import researchstack.data.repository.study.StudyRepositoryImpl
import researchstack.domain.exception.AlreadyJoinedStudy
import researchstack.domain.exception.NotFoundStudyException
import researchstack.domain.model.StudyStatusModel
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.domain.repository.UserStatusRepository
import java.time.LocalDateTime
import java.util.UUID
import researchstack.backend.grpc.Study as StudyProto

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class StudyRepositoryImplTest {
    private val studyDao = spyk<StudyDao>()

    private val studyOutPort = mockk<StudyOutPort>()
    private val userStatusRepository = mockk<UserStatusRepository>()
    private val studyRepository =
        StudyRepositoryImpl(studyDao, GrpcStudyDataSource(studyOutPort), userStatusRepository)

    private val participationCode = "participation code"

    private val studyProto = StudyProto.newBuilder()
        .apply {
            id = UUID.randomUUID().toString()
            participationCode = participationCode
            studyInfo = StudyInfo.newBuilder()
                .apply {
                    name = "Udit's Study"
                    description = "this study is ..."
                    logoUrl = "https://samsung-health-research.com/logo.jpg"
                    organization = "samsung"
                    duration = "15 min per week"
                    period = "3 month"
                }.build()
        }.build()

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchStudiesFromNetwork should save all studies into local repository`() = runTest {
        coEvery { studyOutPort.getPublicStudyList() } returns Result.success(listOf(studyProto))

        studyRepository.fetchStudiesFromNetwork()

        coVerify(exactly = 1) {
            studyDao.insertAll(
                listOf(studyProto).map {
                    it.toDomain().toEntity()
                }
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchStudiesFromNetwork should not throw exception`() = runTest {
        coEvery { studyOutPort.getPublicStudyList() } returns Result.failure(Status.UNKNOWN.asException())

        assertDoesNotThrow { studyRepository.fetchStudiesFromNetwork() }

        coVerify(exactly = 0) { studyDao.insertAll(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchStudyByParticipationCodeFromNetwork should save study into local repository`() =
        runTest {
            coEvery { studyOutPort.getStudyByParticipationCode(any()) } returns Result.success(studyProto)

            studyRepository.fetchStudyByParticipationCodeFromNetwork(participationCode)

            coVerify(exactly = 1) {
                studyDao.insertAll(
                    listOf(studyProto).map {
                        it.toDomain().toEntity()
                    }
                )
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchStudyByParticipationCodeFromNetwork should not throw exception`() = runTest {
        coEvery { studyOutPort.getStudyByParticipationCode(any()) } returns Result.failure(Status.UNKNOWN.asException())

        assertDoesNotThrow {
            studyRepository.fetchStudyByParticipationCodeFromNetwork(
                participationCode
            )
        }

        coVerify(exactly = 0) { studyDao.insertAll(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyByParticipationCode should return study when existed code in local db`() =
        runTest {
            coEvery { studyDao.getStudyByParticipationCode(any()) } returns studyProto.toDomain()
                .toEntity()

            val result = studyRepository.getStudyByParticipationCode(participationCode)

            assertTrue(result.isSuccess)
            assertEquals(studyProto.toDomain(), result.getOrNull())
            coVerify(exactly = 0) { studyOutPort.getStudyByParticipationCode(any()) }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyByParticipationCode should return study when not existed code in local db`() =
        runTest {
            coEvery { studyDao.getStudyByParticipationCode(any()) } returns null
            coEvery { studyOutPort.getStudyByParticipationCode(any()) } returns Result.success(studyProto)

            val result = studyRepository.getStudyByParticipationCode(participationCode)

            assertTrue(result.isSuccess)
            assertEquals(studyProto.toDomain(), result.getOrNull())
            coVerify(exactly = 1) { studyOutPort.getStudyByParticipationCode(any()) }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getStudyByParticipationCode should return failure with NotFoundStudyException when not existed code`() =
        runTest {
            coEvery { studyDao.getStudyByParticipationCode(any()) } returns null
            coEvery { studyOutPort.getStudyByParticipationCode(any()) } returns Result.failure(Status.NOT_FOUND.asException())

            val result = studyRepository.getStudyByParticipationCode(participationCode)

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NotFoundStudyException)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchJoinedStudiesFromNetwork should save joined studies`() = runTest {
        coEvery { studyOutPort.getParticipatedStudyList() } returns Result.success(listOf(studyProto))
        coEvery {
            userStatusRepository.getStudyStatusById(any())
        } returns Result.success(StudyStatusModel.STUDY_STATUS_PARTICIPATING)

        studyRepository.fetchJoinedStudiesFromNetwork()

        coVerify { studyDao.save(any()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchJoinedStudiesFromNetwork should not throw exception`() = runTest {
        coEvery { studyOutPort.getParticipatedStudyList() } returns Result.failure(Status.UNKNOWN.asException())

        assertDoesNotThrow { studyRepository.fetchJoinedStudiesFromNetwork() }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchJoinedStudiesFromNetwork should not throw exception when userStatusRepository#getStudyStatusById failure`() =
        runTest {
            coEvery { studyOutPort.getParticipatedStudyList() } returns Result.success(listOf(studyProto))

            coEvery {
                userStatusRepository.getStudyStatusById(any())
            } returns Result.failure(Exception())

            assertTrue(runCatching { studyRepository.fetchJoinedStudiesFromNetwork() }.isSuccess)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUnregisteredStudies should return unregistered studies`() = runTest {
        coEvery { studyDao.getNotJoinedStudies() } returns flowOf(
            listOf(
                studyProto.toDomain().toEntity()
            )
        )

        studyRepository.getNotJoinedStudies()
            .collect {
                assertEquals(listOf(studyProto.toDomain()), it)
            }

        coVerify(exactly = 1) { studyDao.getNotJoinedStudies() }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyById should return study when existed studyId`() = runTest {
        coEvery { studyDao.getStudyById(any()) } returns flowOf(studyProto.toDomain().toEntity())

        studyRepository.getStudyById("study-id")
            .collect {
                assertEquals(studyProto.toDomain(), it)
            }

        coVerify(exactly = 1) { studyDao.getStudyById(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getJoinedStudies should return unregistered studies`() = runTest {
        coEvery { studyDao.getJoinedStudies() } returns flowOf(
            listOf(
                studyProto.toDomain().toEntity()
            )
        )

        studyRepository.getJoinedStudies()
            .collect {
                assertEquals(listOf(studyProto.toDomain()), it)
            }

        coVerify(exactly = 1) { studyDao.getJoinedStudies() }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `joinStudy should return failure with ALREADY_EXISTS when user already joined given study`() =
        runTest {
            coEvery { studyOutPort.participateInStudy(any(), any(), any()) } returns Result.failure(Status.ALREADY_EXISTS.asException())

            val result = joinStudy()

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AlreadyJoinedStudy)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `joinStudy should set study status as joined`() = runTest {
        coEvery {
            studyOutPort.participateInStudy(any(), any(), any(),)
        } returns Result.success("1")

        val result = joinStudy()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { studyDao.updateJoinedAndRegistrationId(any(), any(), any(), any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `joinStudy should save given studies`() = runTest {
        coJustRun { studyDao.insertAll(any()) }

        studyRepository.insertAll(listOf(studyProto.toDomain()))

        coVerify(exactly = 1) { studyDao.insertAll(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getActiveStudies should save given studies`() = runTest {
        val studyEntity = StudyEntity(
            id = "studyId",
            name = "studyName",
            description = "study description",
            participationCode = "",
            logoUrl = "logourl.com",
            organization = "samsung",
            duration = "10 min/week",
            period = "3 month",
            requirements = emptyList(),
            joined = true,
            registrationId = "1",
        )
        every { studyDao.getActiveStudies() } returns flowOf(listOf(studyEntity))
        val studies = studyRepository.getActiveStudies().first()
        assertEquals(studies[0].id, studyEntity.id)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getActiveStudies should throw Exception`() = runTest {
        every { studyDao.getActiveStudies() } returns flowOf()
        val result = runCatching { studyRepository.getActiveStudies().first() }
        assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `withdrawFromStudy should not throw Exception`() = runTest {
        coEvery { studyOutPort.withdrawFromStudy(any()) } returns Result.success(Unit)
        assertTrue(
            studyRepository.withdrawFromStudy("studyId")
                .onFailure { it.printStackTrace() }.isSuccess
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `withdrawFromStudy should not throw Exception when studyService#withdrawFromStudy Failure`() =
        runTest {
            coEvery { studyOutPort.withdrawFromStudy(any()) } returns Result.failure(Status.UNKNOWN.asException())
            assertTrue(studyRepository.withdrawFromStudy("studyId").isFailure)
        }

    private suspend fun joinStudy() = studyRepository.joinStudy(
        "study-id",
        EligibilityTestResult(
            "study-id",
            SurveyResult(1, LocalDateTime.now(), LocalDateTime.now(), emptyList())
        )
    )
}
