package healthstack.kit.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun KitDatePickerDialog(
    context: Context,
    dateFormat: DateFormat,
    changeValue: (String) -> Unit,
): DatePickerDialog {
    val calendar = Calendar.getInstance()
    calendar.time = Date()

    val mYear: Int = calendar.get(Calendar.YEAR)
    val mMonth: Int = calendar.get(Calendar.MONTH)
    val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val currentValue = remember { mutableStateOf("") }

    return DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            currentValue.value = dateFormat.format(selectedDate.time)
            changeValue(currentValue.value)
        },
        mYear,
        mMonth,
        mDay
    )
}

private fun formatTime(hour: Int, minute: Int): String =
    if (hour == 0) "%02d:%02d AM".format(hour + 12, minute)
    else if (hour < 12) "%02d:%02d AM".format(hour, minute)
    else if (hour == 12) "%02d:%02d PM".format(hour, minute)
    else "%02d:%02d PM".format(hour - 12, minute)

@Composable
fun KitTimePickerDialog(
    context: Context,
    initialHour: Int = 12,
    initialMinute: Int = 0,
    changeValue: (String) -> Unit,
): TimePickerDialog {
    val mHour: Int = initialHour
    val mMinute: Int = initialMinute

    val currentValue = remember { mutableStateOf("") }

    return TimePickerDialog(
        context,
        { _, hour, minute ->
            currentValue.value = formatTime(hour, minute)
            changeValue(currentValue.value)
        },
        mHour,
        mMinute,
        false
    )
}
