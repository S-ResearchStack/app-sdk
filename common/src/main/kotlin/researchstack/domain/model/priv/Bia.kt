package researchstack.domain.model.priv

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset

const val BIA_TABLE_NAME = "bia"

@Entity(
    tableName = BIA_TABLE_NAME
)
data class Bia(
    @PrimaryKey
    override val timestamp: Long = 0,
    val basalMetabolicRate: Float = 0f,
    val bodyFatMass: Float = 0f,
    val bodyFatRatio: Float = 0f,
    val fatFreeMass: Float = 0f,
    val fatFreeRatio: Float = 0f,
    val skeletalMuscleMass: Float = 0f,
    val skeletalMuscleRatio: Float = 0f,
    val totalBodyWater: Float = 0f,
    val measurementProgress: Float = 0f,
    val status: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::basalMetabolicRate.name to basalMetabolicRate,
            ::bodyFatMass.name to bodyFatMass,
            ::bodyFatRatio.name to bodyFatRatio,
            ::fatFreeMass.name to fatFreeMass,
            ::fatFreeRatio.name to fatFreeRatio,
            ::skeletalMuscleMass.name to skeletalMuscleMass,
            ::skeletalMuscleRatio.name to skeletalMuscleRatio,
            ::totalBodyWater.name to totalBodyWater,
            ::measurementProgress.name to measurementProgress,
            ::status.name to status,
            ::timeOffset.name to timeOffset,
        )
}

enum class FailStatusBIA(val status: Int) {
    SENSOR_ERROR(2),
    WRIST_DETACHED(4),
    FINGER_ON_HOME_BUTTON_BROKEN(7),
    FINGER_ON_BACK_BUTTON_BROKEN(8),
    ALL_FINGER_BROKEN(9),
    WRIST_LOOSE(10),
    DRY_FINGER(11),
    BODY_TOO_BIG(13),
    TWO_HAND_TOUCHED_EACH_OTHER(14),
    ALL_FINGER_CONTACT_SUS_FRAME(15),
    UNSTABLE_IMPEDANCE(17),
    BODY_TOO_FAT(18)
}
