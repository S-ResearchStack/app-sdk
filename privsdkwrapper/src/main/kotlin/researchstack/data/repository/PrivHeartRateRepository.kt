package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey.HeartRateSet
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivHeartRateRepository @Inject constructor(
    override val fileRepository: FileRepository<HeartRate>
) :
    WearableDataRepository<HeartRate>, PrivRepository<HeartRate>() {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.HEART_RATE

    override fun receiveDataFlow() = receiveDataPoints().flatten().map {
        it.toModel()
    }

    private fun DataPoint.toModel() = HeartRate(
        timestamp,
        getValue(HeartRateSet.HEART_RATE),
        getValue(HeartRateSet.IBI_LIST),
        getValue(HeartRateSet.IBI_STATUS_LIST),
        getValue(HeartRateSet.HEART_RATE_STATUS)
    )
}
