package healthstack.kit.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme

@Composable
fun AlertPopup(
    initiateText: String,
    title: String,
    body: String,
    confirmText: String,
    dismissText: String,
    onConfirmClicked: () -> Unit = {},
) {
    val popupState = remember { mutableStateOf(false) }

    ClickableText(
        text = AnnotatedString(initiateText),
        style = AppTheme.typography.title3.copy(color = AppTheme.colors.primary),
        onClick = { popupState.value = true }
    )

    if (popupState.value) {
        AlertDialog(
            modifier = Modifier.width(280.dp).shadow(12.dp),
            shape = RoundedCornerShape(8.dp),
            onDismissRequest = { popupState.value = false },
            title = {
                Text(
                    text = title,
                    style = AppTheme.typography.headline3,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                )
            },
            text = {
                Text(
                    text = body,
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.onSurface.copy(0.6F),
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
                )
            },
            confirmButton = {
                ClickableText(
                    text = AnnotatedString(confirmText),
                    style = AppTheme.typography.title3.copy(color = AppTheme.colors.primary),
                    onClick = {
                        onConfirmClicked()
                        popupState.value = false
                    },
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)
                )
            },
            dismissButton = {
                ClickableText(
                    text = AnnotatedString(dismissText),
                    style = AppTheme.typography.title3.copy(color = AppTheme.colors.primary),
                    onClick = { popupState.value = false },
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 6.dp)
                )
            }
        )
    }
}
