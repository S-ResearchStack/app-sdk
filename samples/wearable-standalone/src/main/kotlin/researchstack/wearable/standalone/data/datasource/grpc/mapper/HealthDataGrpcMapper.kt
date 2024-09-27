package researchstack.wearable.standalone.data.datasource.grpc.mapper

import com.google.gson.Gson
import com.google.protobuf.BoolValue
import com.google.protobuf.ByteString
import com.google.protobuf.DoubleValue
import com.google.protobuf.GeneratedMessage
import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import researchstack.backend.grpc.HealthData.Data
import researchstack.backend.grpc.HealthData.HealthDataType
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.model.shealth.SHealthDataType
import java.util.concurrent.TimeUnit
import kotlin.Any
import com.google.protobuf.Any as ProtoAny
import researchstack.backend.grpc.HealthData as GrpcHealthData
import researchstack.domain.model.shealth.SHealthDataType as DomainHealthDataType

private fun DomainHealthDataType.toGrpcHealthDataType() = when (this) {
    SHealthDataType.BLOOD_PRESSURE -> HealthDataType.HEALTH_DATA_TYPE_BLOOD_PRESSURE
    SHealthDataType.HEART_RATE -> HealthDataType.HEALTH_DATA_TYPE_HEART_RATE
    SHealthDataType.SLEEP_SESSION -> HealthDataType.HEALTH_DATA_TYPE_SLEEP_SESSION
    SHealthDataType.SLEEP_STAGE -> HealthDataType.HEALTH_DATA_TYPE_SLEEP_STAGE
    SHealthDataType.STEPS -> HealthDataType.HEALTH_DATA_TYPE_STEPS
    SHealthDataType.WEIGHT -> HealthDataType.HEALTH_DATA_TYPE_WEIGHT
    SHealthDataType.OXYGEN_SATURATION -> HealthDataType.HEALTH_DATA_TYPE_OXYGEN_SATURATION
    SHealthDataType.HEIGHT -> HealthDataType.HEALTH_DATA_TYPE_HEIGHT
    SHealthDataType.RESPIRATORY_RATE -> HealthDataType.HEALTH_DATA_TYPE_RESPIRATORY_RATE
    SHealthDataType.TOTAL_CALORIES_BURNED -> HealthDataType.HEALTH_DATA_TYPE_TOTAL_CALORIES_BURNED
    SHealthDataType.BLOOD_GLUCOSE -> HealthDataType.HEALTH_DATA_TYPE_BLOOD_GLUCOSE
    SHealthDataType.EXERCISE -> HealthDataType.HEALTH_DATA_TYPE_EXERCISE
    SHealthDataType.UNSPECIFIED -> HealthDataType.HEALTH_DATA_TYPE_UNSPECIFIED
}

private fun PrivDataType.toGrpcHealthDataType() = when (this) {
    PrivDataType.WEAR_ACCELEROMETER -> HealthDataType.HEALTH_DATA_TYPE_WEAR_ACCELEROMETER
    PrivDataType.WEAR_BIA -> HealthDataType.HEALTH_DATA_TYPE_WEAR_BIA
    PrivDataType.WEAR_ECG -> HealthDataType.HEALTH_DATA_TYPE_WEAR_ECG
    PrivDataType.WEAR_PPG_GREEN -> HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_GREEN
    PrivDataType.WEAR_PPG_IR -> HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_IR
    PrivDataType.WEAR_PPG_RED -> HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_RED
    PrivDataType.WEAR_SPO2 -> HealthDataType.HEALTH_DATA_TYPE_WEAR_SPO2
    PrivDataType.WEAR_SWEAT_LOSS -> HealthDataType.HEALTH_DATA_TYPE_WEAR_SWEAT_LOSS
    PrivDataType.WEAR_HEART_RATE -> HealthDataType.HEALTH_DATA_TYPE_WEAR_HEART_RATE
}

private fun DeviceStatDataType.toGrpcHealthDataType() = when (this) {
    DeviceStatDataType.MOBILE_WEAR_CONNECTION -> HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_MOBILE_WEAR_CONNECTION
    DeviceStatDataType.WEAR_POWER_ON_OFF -> HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_POWER_ON_OFF
    DeviceStatDataType.WEAR_OFF_BODY -> HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_OFF_BODY
    DeviceStatDataType.WEAR_BATTERY -> HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_BATTERY
}

private val timestampKeySet =
    setOf("time", "starttime", "endtime", "timestamp", "start_time", "end_time", "update_time", "create_time")

private fun isTimeEntry(key: String) =
    timestampKeySet.contains(key.lowercase())

private fun Map.Entry<String, Any>.toProtoType() =
    if (isTimeEntry(key)) {
        ProtoTypes.TIMESTAMP
    } else if (value is Number) {
        when (value) {
            is Byte, is Short, is Int, is Long -> ProtoTypes.LONG
            else -> ProtoTypes.DOUBLE
        }
    } else if (value is Boolean) ProtoTypes.BOOLEAN
    else if (value is Enum<*>) ProtoTypes.LONG
    else if (value is List<*>) ProtoTypes.STRING
    else ProtoTypes.STRING

private const val MILLISECONDS_DIVIDE_BY = 1000

private val gson = Gson()

private fun Map.Entry<String, Any>.toMessage(): GeneratedMessage {
    val anyValue = value

    return if (isTimeEntry(key)) {
        Timestamp.newBuilder()
            .setSeconds(
                TimeUnit.MILLISECONDS.toSeconds(anyValue as Long)
            )
            .setNanos(
                TimeUnit.MILLISECONDS.toNanos(
                    anyValue % MILLISECONDS_DIVIDE_BY
                ).toInt()
            ).build()
    } else if (anyValue is Number) {
        return when (anyValue) {
            is Byte, is Short, is Int, is Long -> Int64Value.newBuilder().setValue(anyValue.toLong()).build()
            else -> DoubleValue.newBuilder().setValue(anyValue.toDouble()).build()
        }
    } else if (anyValue is Boolean) {
        BoolValue.newBuilder().setValue(anyValue).build()
    } else if (anyValue is Enum<*>) {
        Int64Value.newBuilder().setValue(anyValue.ordinal.toLong()).build()
    } else if (anyValue is List<*>) {
        StringValue.newBuilder().setValue(gson.toJson(anyValue)).build()
    } else StringValue.newBuilder().setValue(anyValue as String).build()
}

private fun Map<String, Any>.fromKotlinAnyToProtoAny(): Map<String, ProtoAny> =
    this.mapValues {
        ProtoAny.newBuilder().setTypeUrl(it.toProtoType())
            .setValue(
                ByteString.copyFrom(
                    ProtoAny.pack(it.toMessage()).value.toByteArray()
                )
            )
            .build()
    }

fun HealthDataModel.toData(): GrpcHealthData {
    val grpcHealthDataType = when (unifiedDataType) {
        is DomainHealthDataType -> (unifiedDataType as SHealthDataType).toGrpcHealthDataType()
        is PrivDataType -> (unifiedDataType as PrivDataType).toGrpcHealthDataType()
        is DeviceStatDataType -> (unifiedDataType as DeviceStatDataType).toGrpcHealthDataType()
        else -> HealthDataType.HEALTH_DATA_TYPE_UNSPECIFIED
    }

    return GrpcHealthData.newBuilder()
        .setType(grpcHealthDataType)
        .addAllData(dataList.map { it.toProto() })
        .build()
}

fun Map<String, Any>.toProto(): Data =
    Data.newBuilder().putAllDataMap(fromKotlinAnyToProtoAny()).build()

object ProtoTypes {
    const val TIMESTAMP = "type.googleapis.com/google.protobuf.Timestamp"
    const val DOUBLE = "type.googleapis.com/google.protobuf.DoubleValue"
    const val LONG = "type.googleapis.com/google.protobuf.Int64Value"
    const val STRING = "type.googleapis.com/google.protobuf.StringValue"
    const val BOOLEAN = "type.googleapis.com/google.protobuf.BoolValue"
}
