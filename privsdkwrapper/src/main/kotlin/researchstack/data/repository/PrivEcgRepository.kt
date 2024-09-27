package researchstack.data.repository

import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.priv.Ecg
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class PrivEcgRepository @Inject constructor(
    override val fileRepository: FileRepository<EcgSet>,
) : WearableDataRepository<EcgSet>,
    PrivRepository<EcgSet>() {
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.ECG

    override fun receiveDataFlow(): Flow<EcgSet> = receiveDataPoints().filter {
        it.isNotEmpty()
    }.map {
        it.toECGSet()
    }

    private fun List<DataPoint>.toECGSet(): EcgSet {
        val ppgGreens = mutableListOf(
            this.first().run { PpgGreen(timestamp, getValue(ValueKey.EcgSet.PPG_GREEN)) }
        )
        if (this.size == 10)
            ppgGreens.add(this[5].run { PpgGreen(timestamp, getValue(ValueKey.EcgSet.PPG_GREEN)) })
        return EcgSet(
            this.map { Ecg(it.timestamp, it.getValue(ValueKey.EcgSet.ECG_MV)) },
            ppgGreens,
            this[0].getValue(ValueKey.EcgSet.LEAD_OFF),
            this[0].getValue(ValueKey.EcgSet.MAX_THRESHOLD_MV),
            this[0].getValue(ValueKey.EcgSet.MIN_THRESHOLD_MV),
            this[0].getValue(ValueKey.EcgSet.SEQUENCE)
        )
    }
}
