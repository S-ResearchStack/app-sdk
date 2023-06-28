package healthstack.app.status

import healthstack.app.R
import healthstack.app.viewmodel.HeartRateStatusViewModel

/**
 * An object representing the status of heart rate.
 * Inherits from SampleHealthDataStatus, which represents a type of health data for which status information can be retrieved using a single data point.
 */

object HeartRateStatus : SampleHealthDataStatus("HeartRate") {

    /**
     * Returns the resource ID of the icon representing the heart rate status.
     * @return The resource ID of the icon.
     */
    override fun getIcon(): Int = R.drawable.ic_heart

    /**
     * Returns the unit of measurement for the heart rate data.
     * @return The unit of measurement as a string.
     */

    /**
     * Returns the unit of measurement for the heart rate data.
     * @return The unit of measurement as a string.
     */

    override fun getUnitString(): String = "BPM"

    /**
     * Returns the data key used to extract the heart rate status information.
     * @return The data key.
     */

    override fun getDataKey(): String = "bpm"

    override fun toViewModel() = HeartRateStatusViewModel
}
