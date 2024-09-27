package researchstack.data.repository.healthConnect

import android.util.Log
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import kotlinx.coroutines.flow.first
import researchstack.data.datasource.healthConnect.HealthConnectDataSource
import researchstack.data.datasource.healthConnect.getColumnHeader
import researchstack.data.datasource.healthConnect.processBloodGlucoseData
import researchstack.data.datasource.healthConnect.processBloodPressureData
import researchstack.data.datasource.healthConnect.processHeartRateData
import researchstack.data.datasource.healthConnect.processOxygenSaturationData
import researchstack.data.datasource.healthConnect.processSleepData
import researchstack.data.datasource.healthConnect.processStepsData
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.domain.model.Study
import researchstack.domain.model.healthConnect.HealthConnectDataType
import researchstack.domain.model.healthConnect.HealthConnectDataType.BLOOD_GLUCOSE
import researchstack.domain.model.healthConnect.HealthConnectDataType.BLOOD_PRESSURE
import researchstack.domain.model.healthConnect.HealthConnectDataType.HEART_RATE
import researchstack.domain.model.healthConnect.HealthConnectDataType.OXYGEN_SATURATION
import researchstack.domain.model.healthConnect.HealthConnectDataType.SLEEP_SESSION
import researchstack.domain.model.healthConnect.HealthConnectDataType.STEPS
import researchstack.domain.repository.ShareAgreementRepository
import researchstack.domain.repository.StudyRepository
import researchstack.domain.repository.healthConnect.HealthConnectDataSyncRepository
import researchstack.domain.usecase.file.UploadFileUseCase
import researchstack.util.getDayEndTime
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class HealthConnectDataSyncRepositoryImpl @Inject constructor(
    private val healthConnectDataSource: HealthConnectDataSource,
    private val shareAgreementDao: ShareAgreementDao,
    private val studyRepository: StudyRepository,
    private val shareAgreementRepository: ShareAgreementRepository,
    private val uploadFileUseCase: UploadFileUseCase,
    private val studyDao: StudyDao,
) : HealthConnectDataSyncRepository {
    override suspend fun syncHealthData() {
        getRequiredHealthDataTypes().forEach { dataType ->
            val result: HashMap<Long, StringBuilder>? = when (dataType) {
                STEPS -> processStepsData(
                    healthConnectDataSource.getData(StepsRecord::class)
                        .filterIsInstance<StepsRecord>()
                )

                BLOOD_GLUCOSE -> processBloodGlucoseData(
                    healthConnectDataSource.getData(
                        BloodGlucoseRecord::class
                    ).filterIsInstance<BloodGlucoseRecord>()
                )

                HEART_RATE -> processHeartRateData(
                    healthConnectDataSource.getData(
                        HeartRateRecord::class
                    ).filterIsInstance<HeartRateRecord>()
                )

                OXYGEN_SATURATION -> processOxygenSaturationData(
                    healthConnectDataSource.getData(
                        OxygenSaturationRecord::class
                    ).filterIsInstance<OxygenSaturationRecord>()
                )

                BLOOD_PRESSURE -> processBloodPressureData(
                    healthConnectDataSource.getData(
                        BloodPressureRecord::class
                    ).filterIsInstance<BloodPressureRecord>()
                )

                SLEEP_SESSION -> processSleepData(
                    healthConnectDataSource.getData(
                        SleepSessionRecord::class
                    ).filterIsInstance<SleepSessionRecord>()
                )

                else -> null
            }
            result?.let {
                if (it.isNotEmpty()) uploadDataToServer(dataType, it)
                else Log.e(TAG, "syncHealthConnectData: $dataType list is Empty")
            }
        }
    }

    private suspend fun uploadDataToServer(
        dataType: HealthConnectDataType,
        dataMap: HashMap<Long, StringBuilder>
    ) {
        dataMap.forEach { (dayStartTime, data) ->
            data.insert(0, getColumnHeader(dataType))
            val dayEndTime = getDayEndTime(dayStartTime)
            val inputStream =
                ByteArrayInputStream(data.toString().toByteArray(StandardCharsets.UTF_8))
            studyRepository.getActiveStudies().first().filter {
                shareAgreementRepository.getApprovalShareAgreementWithStudyAndDataType(
                    it.id,
                    dataType.name
                )
            }.forEach { study ->
                val filePath = getFilePath(
                    study,
                    dayStartTime,
                    dayEndTime,
                    dataType.name,
                    "HealthConnect"
                )
                uploadFileUseCase(study.id, filePath, inputStream)
                    .onSuccess {
                        Log.i(
                            TAG,
                            "The file upload containing Health Connect data to S3 was successful, file name: $filePath "
                        )
                    }.onFailure { e ->
                        Log.e(
                            TAG,
                            "The file upload containing Health Connect data to S3 failed, file name: $filePath, Error: ${e.message}"
                        )
                    }.getOrThrow()
            }
        }
    }

    private suspend fun getRequiredHealthDataTypes(): Set<HealthConnectDataType> =
        studyDao.getActiveStudies().first().flatMap { (id) ->
            shareAgreementDao.getAgreedShareAgreement(id).first().map {
                runCatching { HealthConnectDataType.valueOf(it.dataType) }.getOrNull()
            }.filterIsInstance<HealthConnectDataType>()
        }.toSet()

    companion object {
        private val TAG = HealthConnectDataSyncRepositoryImpl::class.simpleName
    }

    private fun getFilePath(
        study: Study,
        dayStartTime: Long,
        dayEndTime: Long,
        dataType: String,
        dataSourceSdk: String = "mobile"
    ): String {
        val directory = "${study.registrationId}/$dataSourceSdk/$dataType"
        val fileName = "${study.id}-${study.registrationId}-$dayStartTime-$dayEndTime-$dataType.csv"
        return "$directory/$fileName"
    }
}
