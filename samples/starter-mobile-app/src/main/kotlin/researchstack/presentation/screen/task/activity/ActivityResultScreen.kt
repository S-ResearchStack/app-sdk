package researchstack.presentation.screen.task.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import researchstack.domain.model.task.activity.ActivityResult
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.ListedText
import researchstack.presentation.component.TopBar
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.viewmodel.task.TaskViewModel

@Composable
internal fun ActivityResultScreen(
    activityResult: ActivityResult,
    taskState: TaskViewModel.TaskState,
    onNextClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(activityResult.title) { }
        },
        bottomBar = {
            Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                AppTextButton(
                    text = activityResult.buttonText,
                    onClick = onNextClick,
                    enabled = taskState == TaskViewModel.TaskState.Init,
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

            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(activityResult.imageId),
                contentDescription = "image for view"
            )

            Spacer(Modifier.height(54.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = activityResult.header,
                style = AppTheme.typography.headline3,
                color = AppTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            ListedText(activityResult.description)
        }
    }
}
