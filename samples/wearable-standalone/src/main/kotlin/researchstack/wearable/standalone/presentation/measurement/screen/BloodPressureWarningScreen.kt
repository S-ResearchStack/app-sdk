package researchstack.wearable.standalone.presentation.measurement.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.component.AppButton
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.theme.HomeScreenItemBackground
import researchstack.wearable.standalone.presentation.theme.TextColor

@Composable
fun BloodPressureWarningScreen(navController: NavHostController) {
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
                textAlign = TextAlign.Center,
                color = TextColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.help_guide_pregnant),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.warning_message_1),
                textAlign = TextAlign.Center,
                color = TextColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.help_guide_avoid),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.warning_message_2),
                textAlign = TextAlign.Center,
                color = TextColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.help_guide_call),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.warning_message_3),
                textAlign = TextAlign.Center,
                color = TextColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppButton(HomeScreenItemBackground, stringResource(id = R.string.ok)) {
                navController.navigate("${Route.Guide.name}/${true}")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
