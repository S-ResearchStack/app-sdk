package researchstack.wearable.standalone.data.datasource.grpc.mapper

import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_ACCELEROMETER
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_BIA
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_ECG
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_HEART_RATE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_GREEN
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_IR
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_RED
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_SPO2
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_SWEAT_LOSS
import researchstack.domain.model.priv.PrivDataType
import researchstack.wearable.standalone.domain.model.ParticipationRequirement
import researchstack.backend.grpc.HealthData.HealthDataType as GrpcHealthDataType

fun GetParticipationRequirementListResponse.toDomain() =
    ParticipationRequirement(
        dataTypesList.map { it.toDomain() }.filterIsInstance<PrivDataType>(),
    )

@Suppress("CyclomaticComplexMethod")
fun GrpcHealthDataType.toDomain() =
    when (this) {
        HEALTH_DATA_TYPE_WEAR_ACCELEROMETER -> PrivDataType.WEAR_ACCELEROMETER
        HEALTH_DATA_TYPE_WEAR_BIA -> PrivDataType.WEAR_BIA
        HEALTH_DATA_TYPE_WEAR_ECG -> PrivDataType.WEAR_ECG
        HEALTH_DATA_TYPE_WEAR_PPG_GREEN -> PrivDataType.WEAR_PPG_GREEN
        HEALTH_DATA_TYPE_WEAR_PPG_IR -> PrivDataType.WEAR_PPG_IR
        HEALTH_DATA_TYPE_WEAR_PPG_RED -> PrivDataType.WEAR_PPG_RED
        HEALTH_DATA_TYPE_WEAR_SPO2 -> PrivDataType.WEAR_SPO2
        HEALTH_DATA_TYPE_WEAR_SWEAT_LOSS -> PrivDataType.WEAR_SWEAT_LOSS
        HEALTH_DATA_TYPE_WEAR_HEART_RATE -> PrivDataType.WEAR_HEART_RATE
        else -> Exception("not supported data type")
    }
