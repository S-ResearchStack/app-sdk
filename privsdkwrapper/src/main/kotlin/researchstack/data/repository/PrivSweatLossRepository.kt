package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.SweatLoss
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivSweatLossRepository @Inject constructor(
    override val fileRepository: FileRepository<SweatLoss>,
) : WearableDataRepository<SweatLoss>, PrivRepository<SweatLoss>() {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.SWEAT_LOSS

    override fun receiveDataFlow() = receiveDataPoints().flatten().map { it.toModel() }

    private fun DataPoint.toModel() = SweatLoss(
        timestamp,
        getValue(ValueKey.SweatLossSet.SWEAT_LOSS),
        getValue(ValueKey.SweatLossSet.STATUS),
    )
}
