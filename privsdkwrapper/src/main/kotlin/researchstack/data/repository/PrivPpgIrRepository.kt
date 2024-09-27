package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivPpgIrRepository @Inject constructor(
    override val fileRepository: FileRepository<PpgIr>,
) :
    PrivRepository<PpgIr>(),
    WearableDataRepository<PpgIr> {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.PPG_IR

    override fun receiveDataFlow() = receiveDataPoints().flatten().map { it.toModel() }

    private fun DataPoint.toModel() = PpgIr(timestamp, getValue(ValueKey.PpgIrSet.PPG_IR))
}
