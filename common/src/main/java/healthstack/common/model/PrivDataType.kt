package healthstack.common.model

enum class PrivDataType(val messagePath: String, val isPassive: Boolean = false) {
    ECG("/ecg_data"),
}
