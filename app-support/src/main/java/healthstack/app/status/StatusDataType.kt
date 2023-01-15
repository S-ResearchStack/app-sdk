package healthstack.app.status

abstract class StatusDataType {
    abstract fun getIcon(): Int

    abstract suspend fun getLatestStatus(): Any?

    abstract fun getUnitString(): String
}
