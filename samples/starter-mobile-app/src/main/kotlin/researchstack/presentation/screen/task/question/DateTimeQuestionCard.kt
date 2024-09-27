package researchstack.presentation.screen.task.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import researchstack.domain.model.task.question.DateTimeQuestion
import researchstack.presentation.component.KitDatePickerDialog
import researchstack.presentation.component.KitTimePickerDialog
import researchstack.presentation.component.RoundTextBoxWithIcon
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale

@Composable
fun DateTimeQuestionCard(question: DateTimeQuestion, onChangedResult: (String) -> Unit) {
    // TODO handle multiple cases
    DateTimeRangeQuestion(question, onChangedResult)
}

@Composable
fun DateTimeRangeQuestion(question: DateTimeQuestion, onChangedResult: (String) -> Unit) {
    var time by remember { mutableStateOf<LocalTime?>(null) }
    var date by remember { mutableStateOf<LocalDate?>(null) }
    val context = LocalContext.current

    val timePickerDialog = KitTimePickerDialog(
        context,
        @Suppress("MagicNumber") 12,
        0
    ) { hour, min ->
        time = LocalTime.of(hour, min)
        date?.let {
            onChangedResult(LocalDateTime.of(date, time).toString())
        }
    }

    val datePickerDialog = KitDatePickerDialog(
        context,
        SimpleDateFormat("MM/dd/yyyy", Locale.US)
    ) { year, month, dayOfMonth ->
        date = LocalDate.of(year, month + 1, dayOfMonth)
        time?.let {
            onChangedResult(LocalDateTime.of(date, time).toString())
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RoundTextBoxWithIcon(
            value = date?.toString() ?: "",
            modifier = Modifier
                .weight(1F)
                .testTag("datePicker"),
            placeholder = "Date",
            icon = Icons.Default.CalendarMonth
        ) {
            datePickerDialog.show()
        }
        Spacer(Modifier.width(8.dp))
        if (question.isTime) {
            RoundTextBoxWithIcon(
                value = time?.toString() ?: "",
                modifier = Modifier
                    .weight(1F)
                    .testTag("timePicker"),
                placeholder = "Time",
            ) {
                timePickerDialog.show()
            }
        }
    }
}
