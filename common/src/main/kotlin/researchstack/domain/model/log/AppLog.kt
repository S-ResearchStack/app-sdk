package researchstack.domain.model.log

import researchstack.common.BuildConfig

open class AppLog(
    val name: String?,
) {
    private val data = mutableMapOf<String, String>()

    init {
        data["appVersion"] = BuildConfig.VERSION_NAME
    }

    fun getData(): Map<String, String> = data

    fun put(key: String, value: String) {
        data[key] = value
    }
}
