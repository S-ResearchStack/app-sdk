package researchstack.domain.model.shealth

data class DataSyncRecord(
    val type: SHealthDataType,
    val timestamp: Long,
    val count: Long,
)
