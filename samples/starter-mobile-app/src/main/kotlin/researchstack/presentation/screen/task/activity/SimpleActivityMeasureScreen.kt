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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import researchstack.domain.model.task.activity.SimpleActivityMeasure
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.CircularTimer
import researchstack.presentation.component.ListedText
import researchstack.presentation.component.TopBar
import researchstack.presentation.theme.AppTheme
import java.time.OffsetDateTime

@Composable
internal fun SimpleActivityMeasureScreen(
    simpleActivityMeasure: SimpleActivityMeasure,
    onComplete: (Map<String, Any>) -> Unit,
) {
    val startTime = OffsetDateTime.now()

    Scaffold(
        topBar = {
            TopBar(simpleActivityMeasure.title) { }
        },
        bottomBar = {
            Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                AppTextButton(
                    text = simpleActivityMeasure.buttonText,
                    onClick = {
                        onComplete(
                            mapOf(
                                "startTime" to startTime.toString(),
                                "endTime" to OffsetDateTime.now().toString(),
                            )
                        )
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

            CircularTimer(
                timeSeconds = simpleActivityMeasure.timeSeconds,
                interactionType = simpleActivityMeasure.interactionType,
            ) {
                onComplete(
                    mapOf(
                        "startTime" to startTime.toString(),
                        "endTime" to OffsetDateTime.now().toString(),
                    )
                )
            }

            Spacer(Modifier.height(54.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = simpleActivityMeasure.header,
                style = AppTheme.typography.headline3,
                color = AppTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            ListedText(simpleActivityMeasure.description)
        }
    }
}
