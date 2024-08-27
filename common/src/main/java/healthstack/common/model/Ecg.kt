package healthstack.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import healthstack.common.util.getCurrentTimeOffset

const val ECG_TABLE_NAME = "ecg"

@Entity(
    tableName = ECG_TABLE_NAME
)
data class EcgSet(
    val ecgs: List<Ecg> = listOf(Ecg(0, .0f)),
    val ppgGreens: List<PpgGreen> = listOf(),
    val leadOff: Int = 0,
    val maxThreshold: Float = .0f,
    val minThreshold: Float = .0f,
    val sequence: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : WearData {
    @PrimaryKey
    override var timestamp = ecgs.first().timestamp

    var sessionId: Long = 0
}

data class Ecg(
    val timestamp: Long = 0,
    val ecg: Float = .0f,
)
