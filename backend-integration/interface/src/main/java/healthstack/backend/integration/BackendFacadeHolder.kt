package healthstack.backend.integration

/**
 * To use the instance as a singleton, we provide holder which holds the single instance.
 */
object BackendFacadeHolder {
    private lateinit var INSTANCE: BackendFacade

    fun initialize(backendFacade: BackendFacade) {
        synchronized(this) {
            if (::INSTANCE.isInitialized.not()) {
                INSTANCE = backendFacade
            }
        }
    }

    fun getInstance(): BackendFacade =
        if (::INSTANCE.isInitialized) INSTANCE
        else throw UninitializedPropertyAccessException()
}
