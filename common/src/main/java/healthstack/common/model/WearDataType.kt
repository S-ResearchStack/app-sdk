package healthstack.common.model

enum class WearDataType(val messagePath: String, val isPassive: Boolean = false) {
    WEAR_ACCELEROMETER("", true),
    WEAR_ECG("/ecg_data"),
    WEAR_HEART_RATE("", true),
    WEAR_PPG_GREEN("", true)
}
