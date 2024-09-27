package researchstack.presentation.screen.task.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.ListedText
import researchstack.presentation.component.TextType
import researchstack.presentation.component.TopBar
import researchstack.presentation.component.Waveform
import researchstack.presentation.theme.AppTheme
import researchstack.util.AudioRecorder

@Composable
internal fun AudioRecordScreen(
    title: String,
    header: String,
    description: List<String>,
    buttonText: String,
    audioRecorder: AudioRecorder = AudioRecorder(LocalContext.current),
    onComplete: (Map<String, Any>) -> Unit,
) {
    val result = mutableMapOf<String, Any>()
    DisposableEffect(Unit) {
        val filePath = audioRecorder.startRecording()
        result["localFilePath"] = filePath

        onDispose {
            audioRecorder.stopRecording()
        }
    }

    Scaffold(
        topBar = {
            TopBar(title) { }
        },
        bottomBar = {
            Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                AppTextButton(
                    text = buttonText,
                    onClick = {
                        onComplete(result)
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(56.dp))

            Waveform(amplitudes = audioRecorder.amplitudes.collectAsState().value)

            Spacer(Modifier.height(54.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = header,
                style = AppTheme.typography.headline3,
                color = AppTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            ListedText(description, type = TextType.NUMBER, textAlign = TextAlign.Left)
        }
    }
}
