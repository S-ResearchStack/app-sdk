package researchstack.data.repository.study

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.entity.ShareAgreementEntity
import researchstack.data.datasource.local.room.mapper.toEntity
import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.domain.model.shealth.SHealthDataType.EXERCISE

internal class ShareAgreementRepositoryImplTest {

    private val shareAgreementDao = mockk<ShareAgreementDao>()
    private val shareAgreementRepository = ShareAgreementRepositoryImpl(shareAgreementDao)

    private val shareAgreement = ShareAgreement(
        studyId = "study-id",
        dataType = EXERCISE,
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `saveShareAgreement should save data in local database`() = runTest {
        coJustRun { shareAgreementDao.insert(any<ShareAgreementEntity>()) }

        val result = shareAgreementRepository.saveShareAgreement(shareAgreement)

        assertTrue(result.isSuccess)
        coVerify { shareAgreementDao.insert(any<ShareAgreementEntity>()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `saveShareAgreement should return failure if failed to save data`() = runTest {
        coEvery { shareAgreementDao.insert(any<ShareAgreementEntity>()) } throws RuntimeException()

        val result = shareAgreementRepository.saveShareAgreement(shareAgreement)

        assertTrue(result.isFailure)
        coVerify { shareAgreementDao.insert(any<ShareAgreementEntity>()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `saveShareAgreements should save data in local database`() = runTest {
        coJustRun { shareAgreementDao.insert(any<List<ShareAgreementEntity>>()) }

        val result = shareAgreementRepository.saveShareAgreements(listOf(shareAgreement))

        assertTrue(result.isSuccess)
        coVerify { shareAgreementDao.insert(any<List<ShareAgreementEntity>>()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `saveShareAgreements should return failure if failed to save data`() = runTest {
        coEvery { shareAgreementDao.insert(any<List<ShareAgreementEntity>>()) } throws RuntimeException()

        val result = shareAgreementRepository.saveShareAgreements(listOf(shareAgreement))

        assertTrue(result.isFailure)
        coVerify { shareAgreementDao.insert(any<List<ShareAgreementEntity>>()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `removeAll should remove all data of given study`() = runTest {
        coJustRun { shareAgreementDao.deleteAll(any()) }

        val result = shareAgreementRepository.removeAll("study")

        assertTrue(result.isSuccess)
        coVerify { shareAgreementDao.deleteAll(any()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeAll should return failure if failed to remove data`() = runTest {
        coEvery { shareAgreementDao.deleteAll(any()) } throws RuntimeException()

        val result = shareAgreementRepository.removeAll("study")

        assertTrue(result.isFailure)
        coVerify { shareAgreementDao.deleteAll(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAgreedHealthDataTypes should all health data types all data of given study`() = runTest {
        every { shareAgreementDao.getAgreedShareAgreement(any()) } returns flowOf(
            listOf(
                shareAgreement.toEntity().apply { id = 3 }
            )
        )

        shareAgreementRepository.getAgreedHealthDataTypes("study-id")
            .collect {
                assertEquals(shareAgreement.dataType, it[0])
            }
        coVerify { shareAgreementDao.getAgreedShareAgreement(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getShareAgreement should remove share-agreement list of given study`() = runTest {
        every { shareAgreementDao.getShareAgreements(any()) } returns flowOf(
            listOf(
                ShareAgreementEntity(shareAgreement.studyId, shareAgreement.dataType.name)
            )
        )

        shareAgreementRepository.getShareAgreement("study")
            .collect {
                assertEquals(shareAgreement, it[0])
            }

        coVerify { shareAgreementDao.getShareAgreements(any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateShareAgreement should update share-agreement`() = runTest {
        coJustRun { shareAgreementDao.updateApproval(any(), any(), any()) }

        val result = shareAgreementRepository.updateShareAgreement(shareAgreement)

        assertTrue(result.isSuccess)
        coVerify { shareAgreementDao.updateApproval(any(), any(), any()) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateShareAgreement should return failure if failed to update`() = runTest {
        coEvery { shareAgreementDao.updateApproval(any(), any(), any()) } throws RuntimeException()

        val result = shareAgreementRepository.updateShareAgreement(shareAgreement)

        assertTrue(result.isFailure)
        coVerify { shareAgreementDao.updateApproval(any(), any(), any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAgreedWearableDataTypes should return original result`() = runTest {
        PrivDataType.entries.forEach {
            val shareAgreementEntity = ShareAgreementEntity("studyId", it.name, true)
            every { shareAgreementDao.getAgreedShareAgreement(any()) } returns flowOf(
                listOf(
                    shareAgreementEntity
                )
            )
            val types = shareAgreementRepository.getAgreedWearableDataTypes("studyId").first()
            assertTrue(types.contains(it))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAgreedWearableDataTypes should return empty when shareAgreementEntity has other type`() =
        runTest {
            SHealthDataType.entries.forEach {
                val shareAgreementEntity = ShareAgreementEntity("studyId", it.name, true)
                every { shareAgreementDao.getAgreedShareAgreement(any()) } returns flowOf(
                    listOf(
                        shareAgreementEntity
                    )
                )
                val types = shareAgreementRepository.getAgreedWearableDataTypes("studyId").first()
                assertTrue(types.isEmpty())
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAgreedWearableDataTypes should not throw Exception when getAgreedShareAgreement empty`() =
        runTest {
            every { shareAgreementDao.getAgreedShareAgreement(any()) } returns flowOf(listOf())
            val types = shareAgreementRepository.getAgreedWearableDataTypes("studyId").first()
            assertTrue(types.isEmpty())
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getApprovalShareAgreementWithStudyAndDataType should return original result`() {
        listOf(true, false).forEach {
            every {
                shareAgreementDao.getApprovalShareAgreementWithStudyAndDataType(
                    any(),
                    any()
                )
            } returns it
            assertTrue(
                shareAgreementRepository.getApprovalShareAgreementWithStudyAndDataType(
                    "studyId",
                    "dataType"
                ) == it
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getApprovalShareAgreementWithStudyAndDataType should throw exception`() {
        every {
            shareAgreementDao.getApprovalShareAgreementWithStudyAndDataType(
                any(),
                any()
            )
        } throws RuntimeException()
        Assertions.assertThrows(RuntimeException::class.java) {
            shareAgreementRepository.getApprovalShareAgreementWithStudyAndDataType(
                "studyId",
                "dataType"
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getShareAgreementFromDataType should return empty list`() = runTest {
        val shareAgreementEntity = ShareAgreementEntity("studyId", "dataType", true)
        every { shareAgreementDao.getAgreedShareAgreementFromDataType(any()) } returns flowOf(
            listOf(
                shareAgreementEntity
            )
        )
        val result = runCatching {
            shareAgreementRepository.getShareAgreementFromDataType(
                "dataType"
            ).first()
        }.onFailure {
            assertTrue(it is NoSuchElementException)
        }
        assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getShareAgreementFromDataType should return not empty list`() = runTest {
        mutableListOf<Enum<*>>().apply {
            addAll(SHealthDataType.entries)
            addAll(TrackerDataType.entries)
            addAll(PrivDataType.entries)
            addAll(DeviceStatDataType.entries)
        }.forEach {
            val shareAgreementEntity = ShareAgreementEntity("studyId", it.name, true)
            every { shareAgreementDao.getAgreedShareAgreementFromDataType(any()) } returns flowOf(
                listOf(
                    shareAgreementEntity
                )
            )
            val shareAgreements = shareAgreementRepository.getShareAgreementFromDataType(
                "dataType"
            ).first()

            assertEquals(shareAgreements[0].dataType, it)
        }
    }
}
