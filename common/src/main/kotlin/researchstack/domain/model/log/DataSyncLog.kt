package researchstack.domain.model.log

class DataSyncLog(message: String) : AppLog("DataSync") {
    init {
        put("message", message)
    }
}
