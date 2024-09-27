package researchstack.data.repository.sensor

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.entity.ShareAgreementEntity
import researchstack.data.datasource.local.room.entity.StudyEntity
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.sensor.TrackerDataType.LIGHT

internal class PermittedSensorTypeRepoImplTest {

    private val studyEntity = StudyEntity(
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

    private val studyDao = mockk<StudyDao> {
        every { getActiveStudies() } returns flowOf(
            listOf(studyEntity)
        )
    }

    private val sharedAgreementEntity = ShareAgreementEntity("studyId", LIGHT.name, true)
    private val shareAgreementDao = mockk<ShareAgreementDao> {
        every { getAgreedShareAgreement(any()) } returns flowOf(
            listOf(
                sharedAgreementEntity
            )
        )
    }

    private val permittedSensorTypeRepo = PermittedSensorTypeRepoImpl(
        studyDao,
        shareAgreementDao
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `getPermittedSensorDataTypeStudyIdMap should return map(sensor to study-list)`() = runTest {
        val typeToStudyList = permittedSensorTypeRepo.getPermittedTypeStudyIdMap()

        val trackerDataType = TrackerDataType.valueOf(sharedAgreementEntity.dataType)
        assertTrue(
            typeToStudyList.keys.contains(trackerDataType)
        )
        val studyIdList = typeToStudyList[trackerDataType] ?: fail()

        assertEquals(listOf(studyEntity.id), studyIdList)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getPermittedSensorDataTypeStudyIdMap should throw exception if studyDao throws exception`() = runTest {
        every { studyDao.getActiveStudies() } throws RuntimeException("")

        assertThrows<Exception> {
            permittedSensorTypeRepo.getPermittedTypeStudyIdMap()
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getPermittedSensorTypes should return set of sensor-types`() = runTest {
        val sensorDataTypes = permittedSensorTypeRepo.getPermittedTypes().first()

        assertTrue(
            sensorDataTypes.contains(TrackerDataType.valueOf(sharedAgreementEntity.dataType))
        )
    }
}
