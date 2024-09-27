package researchstack.wearable.standalone.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import researchstack.wearable.standalone.R

@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogMessage: String,
) {
    Alert(
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 32.dp),
        title = { Text(text = dialogTitle, textAlign = TextAlign.Center) },
        positiveButton = {
            Button(
                colors = ButtonDefaults.secondaryButtonColors(),
                onClick = {
                    onConfirmation()
                }
            ) { Text(stringResource(R.string.permission_alert_confirmation)) }
        },
        negativeButton = {
            Button(
                colors = ButtonDefaults.secondaryButtonColors(),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.permission_alert_dismiss))
            }
        }
    ) {
        Text(
            fontSize = TextUnit(14F, TextUnitType.Sp),
            text = dialogMessage,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AppAlertDialog(dialogTitle: String, dialogMessage: String, confirmButtonText: String, onConfirmation: () -> Unit) {
    Alert(
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 32.dp),
        title = { Text(text = dialogTitle, textAlign = TextAlign.Center) },
        message = {
            Text(
                fontSize = TextUnit(14F, TextUnitType.Sp),
                text = dialogMessage,
                textAlign = TextAlign.Center,
            )
        },
    ) {
        item {
            Chip(
                label = { Text(confirmButtonText) },
                onClick = { onConfirmation() },
                colors = ChipDefaults.primaryChipColors(backgroundColor = MaterialTheme.colors.surface),
            )
        }
    }
}
