package healthstack.healthdata.link.healthconnect

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
//import androidx.health.connect.client.records.ExerciseEventRecord
//import androidx.health.connect.client.records.ExerciseLapRecord
//import androidx.health.connect.client.records.ExerciseRepetitionsRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
//import androidx.health.connect.client.records.HeartRateVariabilityDifferentialIndexRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
//import androidx.health.connect.client.records.HeartRateVariabilitySRecord
//import androidx.health.connect.client.records.HeartRateVariabilitySd2Record
//import androidx.health.connect.client.records.HeartRateVariabilitySdannRecord
//import androidx.health.connect.client.records.HeartRateVariabilitySdnnIndexRecord
//import androidx.health.connect.client.records.HeartRateVariabilitySdnnRecord
//import androidx.health.connect.client.records.HeartRateVariabilitySdsdRecord
//import androidx.health.connect.client.records.HeartRateVariabilityTinnRecord
import androidx.health.connect.client.records.HeightRecord
//import androidx.health.connect.client.records.HipCircumferenceRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SleepStageRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.StepsRecord
//import androidx.health.connect.client.records.SwimmingStrokesRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.Vo2MaxRecord
//import androidx.health.connect.client.records.WaistCircumferenceRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.WheelchairPushesRecord
import kotlin.reflect.KClass

object HealthConnectUtils {
    private val nameToRecordMap: Map<String, KClass<out Record>> =
        mapOf(
            "ActiveCaloriesBurned" to ActiveCaloriesBurnedRecord::class,
//            "ActivityEvent" to ExerciseEventRecord::class, // Keep legacy Activity name
//            "ActivityLap" to ExerciseLapRecord::class, // Keep legacy Activity name
            "ActivitySession" to ExerciseSessionRecord::class, // Keep legacy Activity name
            "BasalBodyTemperature" to BasalBodyTemperatureRecord::class,
            "BasalMetabolicRate" to BasalMetabolicRateRecord::class,
            "BloodGlucose" to BloodGlucoseRecord::class,
            "BloodPressure" to BloodPressureRecord::class,
            "BodyFat" to BodyFatRecord::class,
            "BodyTemperature" to BodyTemperatureRecord::class,
            "BodyWaterMass" to BodyWaterMassRecord::class,
            "BoneMass" to BoneMassRecord::class,
            "CervicalMucus" to CervicalMucusRecord::class,
            "CyclingPedalingCadenceSeries" to
                CyclingPedalingCadenceRecord::class, // Keep legacy Series suffix
            "Distance" to DistanceRecord::class,
            "ElevationGained" to ElevationGainedRecord::class,
            "FloorsClimbed" to FloorsClimbedRecord::class,
            "HeartRate" to HeartRateRecord::class, // Keep legacy Series suffix
//            "HeartRateVariabilityDifferentialIndex" to
//                HeartRateVariabilityDifferentialIndexRecord::class,
            "HeartRateVariabilityRmssd" to HeartRateVariabilityRmssdRecord::class,
//            "HeartRateVariabilityS" to HeartRateVariabilitySRecord::class,
//            "HeartRateVariabilitySd2" to HeartRateVariabilitySd2Record::class,
//            "HeartRateVariabilitySdann" to HeartRateVariabilitySdannRecord::class,
//            "HeartRateVariabilitySdnn" to HeartRateVariabilitySdnnRecord::class,
//            "HeartRateVariabilitySdnnIndex" to HeartRateVariabilitySdnnIndexRecord::class,
//            "HeartRateVariabilitySdsd" to HeartRateVariabilitySdsdRecord::class,
//            "HeartRateVariabilityTinn" to HeartRateVariabilityTinnRecord::class,
            "Height" to HeightRecord::class,
//            "HipCircumference" to HipCircumferenceRecord::class,
            "Hydration" to HydrationRecord::class,
            "LeanBodyMass" to LeanBodyMassRecord::class,
            "Menstruation" to MenstruationFlowRecord::class,
            "Nutrition" to NutritionRecord::class,
            "OvulationTest" to OvulationTestRecord::class,
            "OxygenSaturation" to OxygenSaturationRecord::class,
            "PowerSeries" to PowerRecord::class, // Keep legacy Series suffix
//            "Repetitions" to ExerciseRepetitionsRecord::class, // Keep legacy Repetitions name
            "RespiratoryRate" to RespiratoryRateRecord::class,
            "RestingHeartRate" to RestingHeartRateRecord::class,
            "SexualActivity" to SexualActivityRecord::class,
            "SleepSession" to SleepSessionRecord::class,
            "SleepStage" to SleepStageRecord::class,
            "SpeedSeries" to SpeedRecord::class, // Keep legacy Series suffix
            "Steps" to StepsRecord::class,
            "StepsCadenceSeries" to StepsCadenceRecord::class, // Keep legacy Series suffix
//            "SwimmingStrokes" to SwimmingStrokesRecord::class,
            "TotalCaloriesBurned" to TotalCaloriesBurnedRecord::class,
            "Vo2Max" to Vo2MaxRecord::class,
//            "WaistCircumference" to WaistCircumferenceRecord::class,
            "WheelchairPushes" to WheelchairPushesRecord::class,
            "Weight" to WeightRecord::class,
        )

    fun nameToRecord(name: String) =
        nameToRecordMap[name] ?: throw IllegalArgumentException("Unsupported DataType")
}
