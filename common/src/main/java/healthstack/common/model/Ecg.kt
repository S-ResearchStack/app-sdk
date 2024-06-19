package healthstack.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ECG_TABLE_NAME = "ecg"

@Entity(
    tableName = ECG_TABLE_NAME
)
data class EcgSet(
    val ecgs: List<Ecg>,
    val ppgGreens: List<PpgGreen>,
    val leadOff: Int,
    val maxThreshold: Int,
    val minThreshold: Int,
    val sequence: Int,
) {
    @PrimaryKey
    var timestamp = ecgs.first().timeStamp

    var sessionId: Long = 0
}

data class Ecg(
    val timeStamp: Long,
    val ecg: Int,
)
