package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivAccMeterRepository @Inject constructor(
    override val fileRepository: FileRepository<Accelerometer>
) :
    WearableDataRepository<Accelerometer>, PrivRepository<Accelerometer>() {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.ACCELEROMETER

    override fun receiveDataFlow() = receiveDataPoints().flatten().map { it.toModel() }

    private fun DataPoint.toModel() = Accelerometer(
        timestamp,
        getValue(ValueKey.AccelerometerSet.ACCELEROMETER_X),
        getValue(ValueKey.AccelerometerSet.ACCELEROMETER_Y),
        getValue(ValueKey.AccelerometerSet.ACCELEROMETER_Z)
    )
}
