package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivPpgGreenRepository @Inject constructor(
    override val fileRepository: FileRepository<PpgGreen>
) :
    WearableDataRepository<PpgGreen>,
    PrivRepository<PpgGreen>() {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.PPG_GREEN

    override fun receiveDataFlow() = receiveDataPoints().flatten().map { it.toModel() }

    private fun DataPoint.toModel() = PpgGreen(timestamp, getValue(ValueKey.PpgGreenSet.PPG_GREEN))
}
