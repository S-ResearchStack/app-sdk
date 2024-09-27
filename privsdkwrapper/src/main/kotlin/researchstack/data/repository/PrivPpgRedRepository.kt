package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivPpgRedRepository @Inject constructor(
    override val fileRepository: FileRepository<PpgRed>,
) : WearableDataRepository<PpgRed>,
    PrivRepository<PpgRed>() {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.PPG_RED

    override fun receiveDataFlow() = receiveDataPoints().flatten().map { it.toModel() }

    private fun DataPoint.toModel() = PpgRed(timestamp, getValue(ValueKey.PpgRedSet.PPG_RED))
}
