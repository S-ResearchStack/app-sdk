package healthstack.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PPG_GREEN_TABLE_NAME = "ppg_green"

interface PpgAttribute {
    val ppg: Int
}

sealed class Ppg : PpgAttribute

@Entity(
    tableName = PPG_GREEN_TABLE_NAME,
)
data class PpgGreen(@PrimaryKey val timestamp: Long, override val ppg: Int) : Ppg()
