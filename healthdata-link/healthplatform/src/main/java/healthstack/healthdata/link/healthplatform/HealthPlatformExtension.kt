package healthstack.healthdata.link.healthplatform

import com.google.android.libraries.healthdata.data.DataType
import com.google.android.libraries.healthdata.data.DoubleField
import com.google.android.libraries.healthdata.data.EnumField
import com.google.android.libraries.healthdata.data.IntervalData
import com.google.android.libraries.healthdata.data.LongField
import com.google.android.libraries.healthdata.data.ReadDataResponse
import com.google.android.libraries.healthdata.data.SampleData
import com.google.android.libraries.healthdata.data.SampleDataType
import com.google.android.libraries.healthdata.data.StringField
import healthstack.healthdata.link.HealthData

fun ReadDataResponse.toHealthData(healthDataType: DataType): HealthData {
    val allFields = healthDataType.requiredFields

    val dataSet = if (healthDataType is SampleDataType)
        sampleDataSets
    else
        intervalDataSets

    val healthDataSet = dataSet[0].data
        .map { healthData ->
            allFields.associateTo(mutableMapOf()) {
                it.name to when (it) {
                    is LongField -> healthData.getLongValue(it)
                    is DoubleField -> healthData.getDoubleValue(it)
                    is EnumField -> healthData.getEnumValue(it)
                    is StringField -> healthData.getStringValue(it)
                    else -> Unit
                }
            }.also {
                when (healthDataType) {
                    is SampleDataType -> it[HealthData.TIME_KEY] =
                        (healthData as SampleData).time.toString()
                    else -> {
                        it[HealthData.START_TIME_KEY] =
                            (healthData as IntervalData).startTime.toString()
                        it[HealthData.END_TIME_KEY] = healthData.endTime.toString()
                    }
                }
            }
        }

    return HealthData(healthDataType.name, healthDataSet)
}
