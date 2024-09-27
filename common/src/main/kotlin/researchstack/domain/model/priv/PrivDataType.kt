package researchstack.domain.model.priv

import researchstack.domain.model.shealth.HealthTypeEnum
import kotlin.reflect.KClass

enum class PrivDataType(val messagePath: String, val isPassive: Boolean = false) : HealthTypeEnum {
    WEAR_ACCELEROMETER("/accelerometer_data", true),
    WEAR_BIA("/bia_data"),
    WEAR_ECG("/ecg_data"),
    WEAR_PPG_GREEN("/ppg_green_data", true),
    WEAR_PPG_IR("/ppg_ir_data"),
    WEAR_PPG_RED("/ppg_red_data"),
    WEAR_SPO2("/spo2_data"),
    WEAR_SWEAT_LOSS("/sweat_loss_data"),
    WEAR_HEART_RATE("/heart_rate_data", true),
    ;

    companion object {
        fun fromModel(model: KClass<*>) = when (model) {
            Accelerometer::class -> WEAR_ACCELEROMETER
            Bia::class -> WEAR_BIA
            EcgSet::class -> WEAR_ECG
            PpgGreen::class -> WEAR_PPG_GREEN
            PpgIr::class -> WEAR_PPG_IR
            PpgRed::class -> WEAR_PPG_RED
            SpO2::class -> WEAR_SPO2
            SweatLoss::class -> WEAR_SWEAT_LOSS
            HeartRate::class -> WEAR_HEART_RATE
            else -> throw IllegalArgumentException("${model::class.simpleName} is not PrivDataType")
        }
    }
}
