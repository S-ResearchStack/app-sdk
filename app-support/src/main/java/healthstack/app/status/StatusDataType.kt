package healthstack.app.status

/**
 * An abstract class representing a type of health data for which status information can be retrieved.
 */
abstract class StatusDataType {
    /**
     * Returns the icon resource ID for this health data type.
     * @return The icon resource ID.
     */
    abstract fun getIcon(): Int
    /**
     * Returns the unit string for this health data type.
     * @return The unit string.
     */
    abstract suspend fun getLatestStatus(): Any?
    /**
     * Returns the unit string for this health data type.
     * @return The unit string.
     */
    abstract fun getUnitString(): String
}
