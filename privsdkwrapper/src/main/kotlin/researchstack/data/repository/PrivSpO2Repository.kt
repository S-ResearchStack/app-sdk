package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.SpO2
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivSpO2Repository @Inject constructor(
    override val fileRepository: FileRepository<SpO2>,
) : WearableDataRepository<SpO2>,
    PrivRepository<SpO2>() {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.SPO2

    override fun receiveDataFlow() = receiveDataPoints().flatten().map { it.toModel() }

    private fun DataPoint.toModel() = SpO2(
        timestamp,
        getValue(ValueKey.SpO2Set.HEART_RATE),
        getValue(ValueKey.SpO2Set.SPO2),
        getValue(ValueKey.SpO2Set.STATUS).toSpO2Enum()
    )

    private fun Int.toSpO2Enum(): SpO2.Flag {
        return when (this) {
            -5 -> SpO2.Flag.LOW_SIGNAL
            -4 -> SpO2.Flag.DEVICE_MOVING
            -1 -> SpO2.Flag.INITIAL_STATUS
            0 -> SpO2.Flag.CALCULATING
            2 -> SpO2.Flag.MEASUREMENT_COMPLETED
            else -> SpO2.Flag.FAILED
        }
    }
}
