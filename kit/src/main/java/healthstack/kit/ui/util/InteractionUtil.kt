package healthstack.kit.ui.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import healthstack.kit.ui.util.InteractionType.VIBRATE

object InteractionUtil {
    private fun haptic(context: Context, milliseconds: Long = 500L, amplitude: Int = 100) {
        val vibrator = context
            .getSystemService(Vibrator::class.java)

        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, amplitude))
    }

    fun feedback(context: Context, type: InteractionType) =
        when (type) {
            VIBRATE -> {
                haptic(context)
            }
            else -> {}
        }
}

enum class InteractionType {
    NOTHING, VIBRATE
}
