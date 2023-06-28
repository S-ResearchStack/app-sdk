package healthstack.healthdata.link.healthconnect

import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SleepStageRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.response.ChangesResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import healthstack.healthdata.link.Change
import healthstack.healthdata.link.HealthData

fun ReadRecordsResponse<out Record>.toHealthData(healthDataTypeName: String): HealthData {
    // TODO: refactor logic..
    val healthDataSet = mutableListOf<Map<String, Any>>()

    records.toHealthData(healthDataSet)

    return HealthData(healthDataTypeName, healthDataSet)
}

fun ChangesResponse.toChange(healthDataTypeName: String): Change {
    val healthDataSet = mutableListOf<Map<String, Any>>()

    val newToken = this.nextChangesToken

    this.changes
        .filterIsInstance<UpsertionChange>()
        .map { it.record }
        .toHealthData(healthDataSet)

    return Change(HealthData(healthDataTypeName, healthDataSet), newToken)
}

fun List<Record>.toHealthData(healthDataSet: MutableList<Map<String, Any>>) {
    this.forEach {
        when (it) {
            is HeartRateRecord ->
                it.samples.forEach { sample ->
                    healthDataSet.add(
                        mapOf(
                            "bpm" to sample.beatsPerMinute,
                            HealthData.TIME_KEY to sample.time
                        )
                    )
                }
            is StepsRecord ->
                healthDataSet.add(
                    mapOf(
                        "count" to it.count,
                        HealthData.START_TIME_KEY to it.startTime,
                        HealthData.END_TIME_KEY to it.endTime
                    )
                )
            is SleepSessionRecord ->
                healthDataSet.add(
                    mapOf(
                        HealthData.START_TIME_KEY to it.startTime,
                        HealthData.END_TIME_KEY to it.endTime
                    )
                )

            is SleepStageRecord ->
                healthDataSet.add(
                    mapOf(
                        "stage" to it.stage,
                        HealthData.START_TIME_KEY to it.startTime,
                        HealthData.END_TIME_KEY to it.endTime
                    )
                )
            is BloodPressureRecord ->
                healthDataSet.add(
                    mapOf(
                        HealthData.TIME_KEY to it.time,
                        "systolic" to it.systolic.inMillimetersOfMercury,
                        "diastolic" to it.diastolic.inMillimetersOfMercury,
                    )
                )
            is OxygenSaturationRecord ->
                healthDataSet.add(
                    mapOf(
                        HealthData.TIME_KEY to it.time,
                        "value" to it.percentage.value
                    )
                )
            is WeightRecord ->
                healthDataSet.add(
                    mapOf(
                        HealthData.TIME_KEY to it.time,
                        "kilograms" to it.weight.inKilograms
                    )
                )
            is HeightRecord ->
                healthDataSet.add(
                    mapOf(
                        HealthData.TIME_KEY to it.time,
                        "meters" to it.height.inMeters
                    )
                )
            is TotalCaloriesBurnedRecord ->
                healthDataSet.add(
                    mapOf(
                        "calories" to it.energy.inCalories,
                        HealthData.START_TIME_KEY to it.startTime,
                        HealthData.END_TIME_KEY to it.endTime
                    )
                )
            is RespiratoryRateRecord ->
                healthDataSet.add(
                    mapOf(
                        HealthData.TIME_KEY to it.time,
                        "rpm" to it.rate
                    )
                )
            is BloodGlucoseRecord ->
                healthDataSet.add(
                    mapOf(
                        HealthData.TIME_KEY to it.time,
                        "millimolesPerLiter" to it.level.inMillimolesPerLiter
                    )
                )
            else -> throw RuntimeException("Unsupported Data Type.")
        }
    }
}
