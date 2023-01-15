package healthstack.healthdata.link

object HealthDataLinkHolder {
    private lateinit var INSTANCE: HealthDataLink

    fun initialize(adapter: HealthDataLink) {
        synchronized(this) {
            if (::INSTANCE.isInitialized.not()) {
                INSTANCE = adapter
            }
            // TODO handle multiple time init
        }
    }

    fun getInstance(): HealthDataLink =
        if (::INSTANCE.isInitialized) INSTANCE
        else throw UninitializedPropertyAccessException()
}
