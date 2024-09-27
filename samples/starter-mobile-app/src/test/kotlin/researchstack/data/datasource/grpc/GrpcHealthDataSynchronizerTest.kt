package researchstack.data.datasource.grpc

import io.grpc.Status
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.backend.integration.outport.HealthDataOutPort
import researchstack.domain.model.shealth.HealthDataModel

class GrpcHealthDataSynchronizerTest {
    private val healthData = mockk<HealthDataModel>(relaxed = true)
    private val healthDataOutPort = mockk<HealthDataOutPort> { coEvery { syncBatchHealthData(any(), any()) } returns Result.success(Unit) }
    private val grpcHealthDataSynchronizer = spyk(GrpcHealthDataSynchronizerImpl(healthDataOutPort))

    @Test
    @Tag(NEGATIVE_TEST)
    fun `syncBatchHealthData should return failure`() = runTest {
        coEvery { healthDataOutPort.syncBatchHealthData(any(), any()) } returns Result.failure(Status.NOT_FOUND.asException())
        val res = grpcHealthDataSynchronizer.syncBatchHealthData(listOf("abc"), listOf(healthData))
        Assertions.assertTrue(res.isFailure)
    }
}
