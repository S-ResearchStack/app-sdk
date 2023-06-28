package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.Date
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.DateRange
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.DateTime
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.DateTimeRange
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.Time
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.TimeRange
import healthstack.kit.ui.KitDatePickerDialog
import healthstack.kit.ui.KitTimePickerDialog
import healthstack.kit.ui.RoundTextBoxWithIcon
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class DateTimeQuestionComponent : QuestionComponent<DateTimeQuestionModel>() {
    @Composable
    override fun Render(model: DateTimeQuestionModel, callbackCollection: CallbackCollection) {
        Column {
            super.Render(model, callbackCollection)

            Spacer(Modifier.height(40.dp))

            when (model.viewType) {
                Time -> TimePickerQuestion(model)
                Date -> DatePickerQuestion(model)
                DateTime -> DateTimePickerQuestion(model)
                DateRange -> DateRangeQuestion(model)
                TimeRange -> TimeRangeQuestion(model)
                DateTimeRange -> DateTimeRangeQuestion(model)
            }
        }
    }

    @Composable
    fun TimePickerQuestion(model: DateTimeQuestionModel) {
        val curTime = remember { mutableStateOf("") }
        val updateTime = { newValue: String ->
            curTime.value = newValue
            model.result = newValue
        }
        val context = LocalContext.current

        val timePickerDialog = KitTimePickerDialog(
            context,
            12,
            0,
            updateTime
        )

        RoundTextBoxWithIcon(
            value = curTime.value,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("timePicker"),
            placeholder = "Time"
        ) {
            timePickerDialog.show()
        }
    }

    @Composable
    fun DatePickerQuestion(model: DateTimeQuestionModel) {
        val curDate = remember { mutableStateOf("") }
        val updateDate = { newValue: String ->
            curDate.value = newValue
            model.result = newValue
        }
        val context = LocalContext.current

        val datePickerDialog = KitDatePickerDialog(
            context,
            SimpleDateFormat("MM/dd/yyyy", Locale.US),
            updateDate
        )

        RoundTextBoxWithIcon(
            value = curDate.value,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("datePicker"),
            placeholder = "Date",
            icon = Icons.Default.CalendarMonth
        ) {
            datePickerDialog.show()
        }
    }

    @Composable
    fun DateTimePickerQuestion(model: DateTimeQuestionModel) {
        var resultObject by remember { mutableStateOf(ResultObject()) }
        model.result = resultObject.toString()

        val context = LocalContext.current

        val timePickerDialog = KitTimePickerDialog(
            context,
            12,
            0
        ) {
            resultObject = resultObject.updateStartTime(it)
        }

        val datePickerDialog = KitDatePickerDialog(
            context,
            SimpleDateFormat("MM/dd/yyyy", Locale.US)
        ) {
            resultObject = resultObject.updateStartDate(it)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RoundTextBoxWithIcon(
                value = resultObject.startDate,
                modifier = Modifier
                    .weight(1F)
                    .testTag("datePicker"),
                placeholder = "Date",
                icon = Icons.Default.CalendarMonth
            ) {
                datePickerDialog.show()
            }
            Spacer(Modifier.width(8.dp))
            RoundTextBoxWithIcon(
                value = resultObject.startTime,
                modifier = Modifier
                    .weight(1F)
                    .testTag("timePicker"),
                placeholder = "Time",
            ) {
                timePickerDialog.show()
            }
        }
    }

    @Composable
    fun DateRangeQuestion(model: DateTimeQuestionModel) {
        var resultObject by remember { mutableStateOf(ResultObject()) }
        model.result = resultObject.toString()

        val context = LocalContext.current

        val startDatePickerDialog = KitDatePickerDialog(
            context,
            SimpleDateFormat("MM/dd/yyyy", Locale.US),
        ) {
            resultObject = resultObject.updateStartDate(it)
        }

        val endDatePickerDialog = KitDatePickerDialog(
            context,
            SimpleDateFormat("MM/dd/yyyy", Locale.US),
        ) {
            resultObject = resultObject.updateEndDate(it)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RoundTextBoxWithIcon(
                value = resultObject.startDate,
                modifier = Modifier
                    .weight(1F)
                    .testTag("datePicker"),
                placeholder = "Start",
                icon = Icons.Default.CalendarMonth
            ) {
                startDatePickerDialog.show()
            }
            Spacer(Modifier.width(8.dp))
            RoundTextBoxWithIcon(
                value = resultObject.endDate,
                modifier = Modifier
                    .weight(1F)
                    .testTag("datePicker"),
                placeholder = "End",
                icon = Icons.Default.CalendarMonth
            ) {
                endDatePickerDialog.show()
            }
        }
    }

    @Composable
    fun TimeRangeQuestion(model: DateTimeQuestionModel) {
        var resultObject by remember { mutableStateOf(ResultObject()) }
        model.result = resultObject.toString()

        val context = LocalContext.current

        val startTimePickerDialog = KitTimePickerDialog(
            context,
            12,
            0,
        ) {
            resultObject = resultObject.updateStartTime(it)
        }
        val endTimePickerDialog = KitTimePickerDialog(
            context,
            12,
            0,
        ) {
            resultObject = resultObject.updateEndTime(it)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RoundTextBoxWithIcon(
                value = resultObject.startTime,
                modifier = Modifier
                    .weight(1F)
                    .testTag("timePicker"),
                placeholder = "Start",
            ) {
                startTimePickerDialog.show()
            }
            Spacer(Modifier.width(8.dp))
            RoundTextBoxWithIcon(
                value = resultObject.endTime,
                modifier = Modifier
                    .weight(1F)
                    .testTag("timePicker"),
                placeholder = "End",
            ) {
                endTimePickerDialog.show()
            }
        }
    }

    @Composable
    fun DateTimeRangeQuestion(model: DateTimeQuestionModel) {
        var resultObject by remember { mutableStateOf(ResultObject()) }
        model.result = resultObject.toString()

        val context = LocalContext.current

        val startTimePickerDialog = KitTimePickerDialog(
            context,
            12,
            0,
        ) {
            resultObject = resultObject.updateStartTime(it)
        }
        val endTimePickerDialog = KitTimePickerDialog(
            context,
            12,
            0
        ) {
            resultObject = resultObject.updateEndTime(it)
        }

        val startDatePickerDialog = KitDatePickerDialog(
            context,
            SimpleDateFormat("MM/dd/yyyy", Locale.US)
        ) {
            resultObject = resultObject.updateStartDate(it)
        }

        val endDatePickerDialog = KitDatePickerDialog(
            context,
            SimpleDateFormat("MM/dd/yyyy", Locale.US)
        ) {
            resultObject = resultObject.updateEndDate(it)
        }
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RoundTextBoxWithIcon(
                    value = resultObject.startDate,
                    modifier = Modifier
                        .weight(1F)
                        .testTag("datePicker"),
                    placeholder = "Start",
                    icon = Icons.Default.CalendarMonth
                ) {
                    startDatePickerDialog.show()
                }
                Spacer(Modifier.width(8.dp))
                RoundTextBoxWithIcon(
                    value = resultObject.endDate,
                    modifier = Modifier
                        .weight(1F)
                        .testTag("datePicker"),
                    placeholder = "End",
                    icon = Icons.Default.CalendarMonth
                ) {
                    endDatePickerDialog.show()
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RoundTextBoxWithIcon(
                    value = resultObject.startTime,
                    modifier = Modifier
                        .weight(1F)
                        .testTag("timePicker"),
                    placeholder = "Start",
                ) {
                    startTimePickerDialog.show()
                }
                Spacer(Modifier.width(8.dp))
                RoundTextBoxWithIcon(
                    value = resultObject.endTime,
                    modifier = Modifier
                        .weight(1F)
                        .testTag("timePicker"),
                    placeholder = "End",
                ) {
                    endTimePickerDialog.show()
                }
            }
        }
    }
}

data class ResultObject(
    val startTime: String = "",
    val endTime: String = "",
    val startDate: String = "",
    val endDate: String = "",
) {
    fun updateStartTime(value: String) = this.copy(startTime = value)

    fun updateEndTime(value: String) = this.copy(endTime = value)

    fun updateStartDate(value: String) = this.copy(startDate = value)

    fun updateEndDate(value: String) = this.copy(endDate = value)

    override fun toString(): String {
        val ret = mutableMapOf<Any?, Any?>()

        if (this.startTime.isNotEmpty()) ret["start_time"] = this.startTime
        if (this.endTime.isNotEmpty()) ret["end_time"] = this.endTime
        if (this.startDate.isNotEmpty()) ret["start_date"] = this.startDate
        if (this.endDate.isNotEmpty()) ret["end_date"] = this.endDate

        return JSONObject(ret).toString()
    }
}
