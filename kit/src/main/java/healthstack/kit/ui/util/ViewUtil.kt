package healthstack.kit.ui.util

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Snackbar
import androidx.compose.material.TextButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme

object ViewUtil {
    fun showToastMessage(context: Context, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_LONG)
            .show()
    @Composable
    fun SetSnackbar(
        snackbarHostState: SnackbarHostState,
        modifier: Modifier = Modifier,
        onAction: () -> Unit = {}
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    modifier = Modifier.padding(0.dp),
                    shape = RoundedCornerShape(2.dp),
                    action = {
                        snackbarHostState.currentSnackbarData?.actionLabel?.let { actionLabel ->
                            TextButton(onClick = onAction) {
                                Text(
                                    text = actionLabel,
                                    color = AppTheme.colors.onPrimary,
                                    style = AppTheme.typography.title3
                                )
                            }
                        }
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = snackbarHostState.currentSnackbarData?.message ?: "",
                        color = AppTheme.colors.onPrimary,
                        style = AppTheme.typography.body2
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
        )
    }
}
