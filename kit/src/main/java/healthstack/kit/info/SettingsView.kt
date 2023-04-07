package healthstack.kit.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.ui.AlertPopup
import healthstack.kit.ui.TopBar

class SettingsView(
    val onClickBack: () -> Unit = {},
    val initialize: () -> Unit = {},
) {
    @Composable
    fun Render() {
        val scrollState = rememberScrollState()

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

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
fun SettingsPreview() =
    SettingsView().Render()
