package healthstack.app.status

import healthstack.app.R

object HeartRateStatus : SampleHealthDataStatus("HeartRateSeries") {
    override fun getIcon(): Int = R.drawable.ic_heart

    override fun getUnitString(): String = "\nBPM"

    override fun getDataKey(): String = "bpm"
}
