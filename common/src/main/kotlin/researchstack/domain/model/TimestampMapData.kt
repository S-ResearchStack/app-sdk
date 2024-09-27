package researchstack.domain.model

interface TimestampMapData : MapData, Timestamp {
    fun toTimeDataMap(): Map<String, Any> = mapOf(
        "timestamp" to timestamp,
        "time_offset" to timeOffset
    )
}
