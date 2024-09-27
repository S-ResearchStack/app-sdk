package researchstack.presentation.measurement.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import researchstack.R
import researchstack.presentation.component.AppButton
import researchstack.presentation.measurement.Route
import researchstack.presentation.theme.ItemHomeColor
import researchstack.presentation.theme.TextGray
import researchstack.presentation.theme.Typography

@Composable
fun BiaGuideScreen(navController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.warning),
                color = TextGray,
                style = Typography.body1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.bia_warning_1), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.bia_warning_2), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            AppButton(ItemHomeColor, stringResource(id = R.string.ok)) {
                navController.navigate(Route.Measure.name)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
