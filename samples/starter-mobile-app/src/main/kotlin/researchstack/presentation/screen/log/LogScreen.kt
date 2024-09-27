package researchstack.presentation.screen.log

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.presentation.component.TopBar
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.viewmodel.log.AppLogViewModel

@Composable
fun LogScreen(
    appLogViewModel: AppLogViewModel = hiltViewModel(),
) {
    val logs = appLogViewModel.appLogs.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(1f),
        topBar = {
            TopBar(title = "Logs") {
                IconButton(onClick = {
                    appLogViewModel.refresh()
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "refresh",
                        tint = AppTheme.colors.onSurface,
                    )
                }
            }
        },
    ) { innerPadding ->
        SelectionContainer {
            LazyColumn(
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 8.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                logs.forEach { log ->
                    item {
                        val time = log.getData()["time"]
                        val message = log.getData()["message"]
                        Text("$time: $message")
                    }
                }
            }
        }
    }
}
