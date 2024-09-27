package researchstack.domain.model

data class Event(
    val timeStamp: Long,
    val studyId: String,
    val name: String,
) {
    private val data = mutableMapOf<String, String>()

    fun getData(): Map<String, String> = data

    fun put(key: String, value: String) {
        data[key] = value
    }

    fun putAll(from: Map<String, String>) {
        data.putAll(from)
    }
}
