package researchstack.presentation.component

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.text.DateFormat
import java.util.Date

@Composable
fun KitDatePickerDialog(
    context: Context,
    dateFormat: DateFormat,
    changeValue: (Int, Int, Int) -> Unit,
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
            changeValue(year, month, dayOfMonth)
        },
        mYear,
        mMonth,
        mDay
    )
}
