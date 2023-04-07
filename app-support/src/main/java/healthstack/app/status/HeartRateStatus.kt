package healthstack.app.status

import healthstack.app.R

object HeartRateStatus : SampleHealthDataStatus("HeartRate") {
    override fun getIcon(): Int = R.drawable.ic_heart

    override fun getUnitString(): String = "BPM"

    override fun getDataKey(): String = "bpm"
}
