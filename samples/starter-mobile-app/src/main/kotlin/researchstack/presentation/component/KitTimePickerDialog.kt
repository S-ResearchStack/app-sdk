package researchstack.presentation.component

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun KitTimePickerDialog(
    context: Context,
    initialHour: Int = 12,
    initialMinute: Int = 0,
    changeValue: (Int, Int) -> Unit,
): TimePickerDialog {
    val mHour: Int = initialHour
    val mMinute: Int = initialMinute

    val currentValue = remember { mutableStateOf("") }

    return TimePickerDialog(
        context,
        { _, hour, minute ->
            currentValue.value = formatTime(hour, minute)
            changeValue(hour, minute)
        },
        mHour,
        mMinute,
        false
    )
}

@Suppress("MagicNumber")
private fun formatTime(hour: Int, minute: Int): String =
    if (hour == 0) "%02d:%02d AM".format(12, minute)
    else if (hour < 12) "%02d:%02d AM".format(hour, minute)
    else if (hour == 12) "%02d:%02d PM".format(hour, minute)
    else "%02d:%02d PM".format(hour - 12, minute)
