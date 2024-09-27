package researchstack.data.repository.sensor

import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.repository.PermittedDataTypeRepository
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.repository.sensor.PermittedSensorTypeRepository
import javax.inject.Inject

class PermittedSensorTypeRepoImpl @Inject constructor(
    override val studyDao: StudyDao,
    override val shareAgreementDao: ShareAgreementDao
) : PermittedSensorTypeRepository, PermittedDataTypeRepository<TrackerDataType>() {
    override val tClass = TrackerDataType::class
}
