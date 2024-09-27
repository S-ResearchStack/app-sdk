package researchstack.data.datasource.grpc

import io.grpc.Status
import io.grpc.StatusException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.grpc.Study
import researchstack.backend.grpc.StudyInfo
import researchstack.backend.integration.outport.StudyOutPort
import researchstack.data.datasource.grpc.mapper.toDomain
import java.util.UUID

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class GrpcStudyDataSourceTest {

    private val studyOutPort = mockk<StudyOutPort>()
    private val grpcStudyDataSource = GrpcStudyDataSource(studyOutPort)

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyList should return success with study list`() = runTest {
        val grpcStudies = listOf(
            Study.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setStudyInfo(
                    StudyInfo.newBuilder().apply {
                        name = "study-name"
                        description = "description for study"
                        logoUrl = "https://study.log.url"
                    }
                )
                .build()
        )

        coEvery { studyOutPort.getPublicStudyList() } returns Result.success(grpcStudies)

        val result = grpcStudyDataSource.getStudyList()

        assertTrue(result.isSuccess)
        assertEquals(grpcStudies.map { it.toDomain() }, result.getOrThrow())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getStudyList should return failure if not registered user `() = runTest {
        coEvery { studyOutPort.getPublicStudyList() } returns Result.failure(Status.NOT_FOUND.asException())

        val result = grpcStudyDataSource.getStudyList()

        assertTrue(result.isFailure)
        assertEquals(Status.NOT_FOUND, (result.exceptionOrNull() as? StatusException)?.status)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `withdrawFromStudy should return success with study`() = runTest {
        coEvery { studyOutPort.withdrawFromStudy(any()) } returns Result.success(Unit)
        val res = grpcStudyDataSource.withdrawFromStudy("abc")
        assertTrue(res.isSuccess)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `withdrawFromStudy should return failure with study`() = runTest {
        coEvery { studyOutPort.withdrawFromStudy(any()) } returns Result.failure(Status.NOT_FOUND.asException())
        val res = grpcStudyDataSource.withdrawFromStudy("abc")
        assertTrue(res.isFailure)
    }
}
