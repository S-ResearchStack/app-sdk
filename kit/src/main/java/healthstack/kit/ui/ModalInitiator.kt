package healthstack.kit.ui

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalInitiator(
    id: String,
    title: String,
    placeholder: String,
    state: ModalBottomSheetState,
    selectedValue: MutableState<String>,
    changeModal: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth(),
        style = AppTheme.typography.body3,
        color = AppTheme.colors.onBackground.copy(0.6F)
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = selectedValue.value.ifEmpty { placeholder },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                scope.launch {
                    changeModal(id)
                    state.show()
                }
            },
        style = AppTheme.typography.body1,
        color =
        if (selectedValue.value.isEmpty()) AppTheme.colors.onBackground.copy(0.6F)
        else AppTheme.colors.onBackground
    )
    Spacer(Modifier.height(4.dp))
    Divider(
        color =
        if (selectedValue.value.isEmpty()) AppTheme.colors.onBackground.copy(0.6F)
        else AppTheme.colors.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(32.dp))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarModalInitiator(
    title: String,
    placeholder: String,
    changeValue: (String) -> Unit,
) {

    val calendar = Calendar.getInstance()
    calendar.time = Date()

    val mYear: Int = calendar.get(Calendar.YEAR)
    val mMonth: Int = calendar.get(Calendar.MONTH)
    val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val currentValue = remember { mutableStateOf("") }
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            currentValue.value = "%02d / %02d / %04d".format(dayOfMonth, month + 1, year)
            Log.e("Date", currentValue.value)
            changeValue(currentValue.value)
        },
        mYear,
        mMonth,
        mDay
    )

    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth(),
        style = AppTheme.typography.body3,
        color = AppTheme.colors.onBackground.copy(0.6F)
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = currentValue.value.ifEmpty { placeholder },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                datePickerDialog.show()
            },
        style = AppTheme.typography.body1,
        color =
        if (currentValue.value.isEmpty()) AppTheme.colors.onBackground.copy(0.6F)
        else AppTheme.colors.onBackground
    )
    Spacer(Modifier.height(4.dp))
    Divider(
        color =
        if (currentValue.value.isEmpty()) AppTheme.colors.onBackground.copy(0.6F)
        else AppTheme.colors.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(32.dp))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChoiceModal(
    title: String,
    values: List<String>,
    changeValue: (String) -> Unit,
    state: ModalBottomSheetState,
) {
    val currentValue = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.headline3,
            color = AppTheme.colors.onBackground
        )
        values.forEach { value ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .let {
                        if (currentValue.value == value) {
                            return@let it.background(
                                AppTheme.colors.primary.copy(0.08F)
                            )
                        }
                        it
                    }
            ) {
                RadioButton(
                    selected = currentValue.value == value,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 24.dp, end = 12.dp),
                    onClick = {
                        currentValue.value = value
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colors.primary,
                        unselectedColor = AppTheme.colors.primaryVariant,
                        disabledColor = AppTheme.colors.disabled,
                    )
                )
                Spacer(Modifier.padding(horizontal = 12.dp))
                Text(
                    text = value,
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 25.dp)
        ) {
            Text(
                text = "Cancel",
                style = AppTheme.typography.title1,
                color = AppTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(150.dp)
                    .clickable {
                        currentValue.value = ""
                        scope.launch { state.hide() }
                    }
            )
            Text(
                text = "Save",
                style = AppTheme.typography.title1,
                color = AppTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(150.dp)
                    .clickable {
                        changeValue(currentValue.value)
                        scope.launch { state.hide() }
                    }
            )
        }
    }
}
