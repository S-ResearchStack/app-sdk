package researchstack.presentation.util

import researchstack.R.string
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType

private fun SHealthDataType.toStringResourceId(): Int =
    when (this) {
        SHealthDataType.BLOOD_GLUCOSE -> string.blood_glucose
        SHealthDataType.BLOOD_PRESSURE -> string.blood_pressure
        SHealthDataType.EXERCISE -> string.exercise
        SHealthDataType.HEART_RATE -> string.heartrate
        SHealthDataType.HEIGHT -> string.height
        SHealthDataType.OXYGEN_SATURATION -> string.spo2
        SHealthDataType.SLEEP_SESSION -> string.sleep_session
        SHealthDataType.SLEEP_STAGE -> string.sleep_stage
        SHealthDataType.STEPS -> string.steps
        SHealthDataType.WEIGHT -> string.weight
        SHealthDataType.RESPIRATORY_RATE -> string.respiratory_rate
        SHealthDataType.TOTAL_CALORIES_BURNED -> string.total_calories_burned
        SHealthDataType.UNSPECIFIED -> string.unspecified
    }

private fun TrackerDataType.toStringResourceId(): Int =
    when (this) {
        TrackerDataType.LIGHT -> string.light
        TrackerDataType.ACCELEROMETER -> string.accelerometer
        TrackerDataType.SPEED -> string.speed
    }

private fun PrivDataType.toStringResourceId(): Int =
    when (this) {
        PrivDataType.WEAR_ACCELEROMETER -> string.wear_acceleromemter
        PrivDataType.WEAR_BIA -> string.wear_bia
        PrivDataType.WEAR_ECG -> string.wear_ecg
        PrivDataType.WEAR_PPG_GREEN -> string.wear_ppg_green
        PrivDataType.WEAR_PPG_IR -> string.wear_ppg_ir
        PrivDataType.WEAR_PPG_RED -> string.wear_ppg_red
        PrivDataType.WEAR_SPO2 -> string.wear_spo2
        PrivDataType.WEAR_SWEAT_LOSS -> string.wear_sweat_loss
        PrivDataType.WEAR_HEART_RATE -> string.wear_heart_rate
    }

private fun DeviceStatDataType.toStringResourceId(): Int =
    when (this) {
        DeviceStatDataType.MOBILE_WEAR_CONNECTION -> string.device_stat_mobile_wear_connection
        DeviceStatDataType.WEAR_OFF_BODY -> string.device_stat_wear_off_body
        DeviceStatDataType.WEAR_BATTERY -> string.device_stat_wear_battery
        DeviceStatDataType.WEAR_POWER_ON_OFF -> string.device_stat_wear_power_on_off
    }

fun Enum<*>.toStringResourceId() =
    when (this) {
        is SHealthDataType -> this.toStringResourceId()
        is TrackerDataType -> this.toStringResourceId()
        is PrivDataType -> this.toStringResourceId()
        is DeviceStatDataType -> this.toStringResourceId()
        else -> throw IllegalArgumentException("Not healthDataType")
    }
