package healthstack.kit.info

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.datastore.PreferenceDataStore
import healthstack.kit.notification.AlarmUtils
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.AlertPopup
import healthstack.kit.ui.ToggleSwitch
import healthstack.kit.ui.TopBar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsView(
    val onClickBack: () -> Unit = {},
    val initialize: () -> Unit = {},
) {
    @Composable
    fun Render() {
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        val preferenceDataStore = PreferenceDataStore(context)
        val alarmUtils = AlarmUtils.getInstance()
        val notificationSchedule = remember { mutableStateOf("") }
        val notificationStatus = remember { mutableStateOf(false) }
        val changeStatus = fun(status: Boolean) {
            coroutineScope.launch {
                notificationStatus.value = status
                if (notificationStatus.value)
                    alarmUtils.setRepeatingAlarm(
                        AlarmUtils.toTimeInMillis(notificationSchedule.value),
                        1000 * 60 * 60 * 24, // timeInMillis for one day
                        AlarmUtils.REMINDER_ALARM_CODE
                    )
                else
                    alarmUtils.cancelAlarm(AlarmUtils.REMINDER_ALARM_CODE)

                preferenceDataStore.setPush(notificationStatus.value)
            }
        }
        val changeSchedule = fun(schedule: String) {
            coroutineScope.launch {
                alarmUtils.cancelAlarm(AlarmUtils.REMINDER_ALARM_CODE)
                notificationSchedule.value = schedule
                preferenceDataStore.setReminder(notificationSchedule.value)
                alarmUtils.setRepeatingAlarm(
                    AlarmUtils.toTimeInMillis(notificationSchedule.value),
                    1000 * 60 * 60 * 24, // timeInMillis for one day
                    AlarmUtils.REMINDER_ALARM_CODE
                )
            }
        }

        LaunchedEffect(Unit) {
            notificationStatus.value = preferenceDataStore.push.first()
            notificationSchedule.value = preferenceDataStore.reminder.first()
        }

        Scaffold(
            topBar = {
                TopBar("Settings") {
                    onClickBack()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 24.dp)
                    .verticalScroll(scrollState),
            ) {
                Text(
                    text = "Reminders",
                    style = AppTheme.typography.title2,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Select the time for assessment reminders.",
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.onSurface.copy(0.6F),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(26.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "1st Time",
                            style = AppTheme.typography.body1,
                            color = AppTheme.colors.onBackground,
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            NotificationTimePicker(
                                notificationStatus.value,
                                notificationSchedule.value,
                                changeSchedule
                            )
                            Spacer(Modifier.width(10.dp))
                            Icon(
                                imageVector = Icons.Filled.Alarm,
                                tint = if (notificationStatus.value) AppTheme.colors.primary
                                else AppTheme.colors.primary.copy(0.38F),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(46.dp))
                ReminderSwitch(initialState = notificationStatus.value, changeState = changeStatus)
                Spacer(Modifier.height(65.dp))
                AlertPopup(
                    "Logout",
                    "Close this App?",
                    "Are you sure you want to logout?",
                    "Logout",
                    "Cancel",
                ) {
                    Firebase.auth.signOut()
                    initialize()
                }
                Spacer(Modifier.height(30.dp))
                AlertPopup(
                    "Withdraw from Study",
                    "Withdraw from Study?",
                    "Withdrawing from the study will permanently delete your account. " +
                        "Are you sure you would like to proceed?",
                    "Withdraw",
                    "Cancel",
                )
            }
        }
    }
}

@Composable
private fun ReminderSwitch(
    description: String = "Select the way to receive your reminders:",
    initialState: Boolean,
    changeState: (Boolean) -> Unit,
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = description,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.onSurface.copy(0.6F),
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(18.dp))
        ToggleSwitch("Push", initialState, changeState)
    }
}

object TimeUtils {
    fun formatTime(hour: Int, minute: Int): String =
        if (hour == 0) "%02d:%02d AM".format(hour + 12, minute)
        else if (hour < 12) "%02d:%02d AM".format(hour, minute)
        else if (hour == 12) "%02d:%02d PM".format(hour, minute)
        else "%02d:%02d PM".format(hour - 12, minute)
}

@Composable
private fun NotificationTimePicker(
    initialStatus: Boolean,
    placeholder: String,
    changeValue: (String) -> Unit,
) {
    val mHour = 12
    val mMinute = 0
    val currentValue = remember { mutableStateOf("") }
    val context = LocalContext.current

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            currentValue.value = TimeUtils.formatTime(hour, minute)
            changeValue(currentValue.value)
        },
        mHour,
        mMinute,
        false
    )

    Text(
        text = currentValue.value.ifEmpty { placeholder },
        modifier = Modifier
            .clickable(
                enabled = initialStatus
            ) {
                timePickerDialog.show()
            },
        style = AppTheme.typography.title2,
        color = if (initialStatus) AppTheme.colors.primary else AppTheme.colors.primary.copy(0.38F),
        textAlign = TextAlign.Right
    )
}

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
fun SettingsPreview() =
    SettingsView().Render()
